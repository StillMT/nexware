package it.unisa.nexware.storage.dao;

import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import static it.unisa.nexware.storage.dao.CompanyDAO.checkAvailability;

public class NewsletterDAO {

    // Attributi
    private static final Logger logger = Logger.getLogger(NewsletterDAO.class.getName());

    public static boolean doCheckEmailAvailability(String email) {
        final String sql = "SELECT email FROM newsletter_email WHERE email = ?";

        return checkAvailability(email, sql);
    }

    public static boolean doInsertEmail(String email) {
        final String sql = "INSERT INTO newsletter_email (email) VALUES(?)";
        boolean result = false;

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;

            ps = con.prepareStatement(sql);
            ps.setString(1, email);

            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps);
        }

        return result;
    }


}
