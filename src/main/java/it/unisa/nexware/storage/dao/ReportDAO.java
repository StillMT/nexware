package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.ReportBean;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ReportDAO {

    // Attributi
    private static final Logger logger = Logger.getLogger(ReportDAO.class.getName());

    public static boolean doInsertReport(ReportBean r) {
        final String sql = "INSERT INTO report VALUES (NULL, ?, ?, ?, ?)";
        boolean result = false;

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;

            ps = con.prepareStatement(sql);

            ps.setString(1, r.getCompanyName());
            ps.setString(2, r.getEmail());
            ps.setString(3, r.getObject());
            ps.setString(4, r.getDescription());

            if (ps.executeUpdate() == 1)
                result = true;
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps);
        }

        return result;
    }
}
