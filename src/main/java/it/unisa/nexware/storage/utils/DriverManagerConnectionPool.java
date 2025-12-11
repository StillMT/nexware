package it.unisa.nexware.storage.utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DriverManagerConnectionPool {

    // Costruttore privato per evitare di instanziare
    private DriverManagerConnectionPool() {}

    // Attributi
    private static final String DB_URL = "jdbc:mysql://nexware.ddns.net:3306/nexware";
    private static final String DB_USER = "nexware_admin";
    private static final String DB_PASSWORD = "tV9!mF5rJ0$uR2qZ_^e8Gx#Bn47pKs%wEh@Y3aVd*L6zHt!";
    private static final long MAX_IDLE_TIME = 120_000; // 2 minuti
    private static final int CHECK_PERIOD = 30;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final Logger logger = Logger.getLogger(DriverManagerConnectionPool.class.getName());

    private static class PooledConnection {
        Connection connection;
        long releaseTime;

        PooledConnection(Connection connection) {
            this.connection = connection;
            this.releaseTime = System.currentTimeMillis();
        }
    }

    private static final List<PooledConnection> freeDbConnections = new LinkedList<>();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver DB non trovato: " + e.getMessage());
        }

        // Thread che rimuove connessioni inattive
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (DriverManagerConnectionPool.class) {
                closeConnections(false);
            }
        }, 1, CHECK_PERIOD, TimeUnit.SECONDS);
    }

    private static synchronized Connection createDBConnection() {
        Connection newConnection = null;

        try {
            newConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            newConnection.setAutoCommit(true);

            logConnectionStatuses("Creating new connection: " + newConnection);
        } catch (SQLException e) {
            logSqlError(e);
        }

        return newConnection;
    }

    public static synchronized Connection getConnection() {
        while (!freeDbConnections.isEmpty()) {
            Connection c = freeDbConnections.removeFirst().connection;
            try {
                if (!c.isClosed() && c.isValid(2)) {
                    logConnectionStatuses("Taking from pool: " + c);

                    return c;
                }
            } catch (SQLException e) {
                logSqlError(e);
            }
        }

        return createDBConnection();
    }

    public static synchronized void releaseConnection(Connection c) {
        try {
            if (c != null && !c.isClosed() && c.isValid(2)) {
                logConnectionStatuses("Adding to pool: " + c);

                freeDbConnections.add(new PooledConnection(c));
            }
        } catch (SQLException e) {
            logSqlError(e);
        }
    }

    public static void closeSqlParams(Connection c, PreparedStatement ps) {
        releaseConnection(c);

        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            logSqlError(e);
        }
    }

    public static void closeSqlParams(Connection c, PreparedStatement ps, ResultSet rs) {
        closeSqlParams(c, ps);

        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            logSqlError(e);
        }
    }

    public static void shutdown() {
        scheduler.shutdown();
        logConnectionStatuses("Shutting down detected, closing connections...");
        closeConnections(true);
    }

    public static void logSqlError(SQLException e, Logger logger) {
        logger.log(Level.SEVERE, "SQL error", e);
    }

    private static void logSqlError(SQLException e) {
        logSqlError(e, logger);
    }

    private static void logConnectionStatuses(String message) {
        System.out.println("[" + LocalDateTime.now() + "] " + message);
    }

    private static void closeConnections(boolean all) {
        long now = System.currentTimeMillis();

        for (PooledConnection pc : freeDbConnections)
            if (all || now - pc.releaseTime > MAX_IDLE_TIME) {
                try {
                    try {
                        logConnectionStatuses("Closing: " + pc.connection);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    pc.connection.close();
                    freeDbConnections.remove(pc);
                } catch (SQLException e) {
                    logSqlError(e);
                }
            }
    }
}