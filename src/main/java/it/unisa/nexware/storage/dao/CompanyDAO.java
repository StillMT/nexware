package it.unisa.nexware.storage.dao;

import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class CompanyDAO {

    // Attributi
    private static final Logger logger = Logger.getLogger(CompanyDAO.class.getName());

    // Metodi pubblici
    public static boolean doCheckUsernameAvailability(String username) {
        final String sql = "SELECT username FROM company WHERE username = ?";

        return checkAvailability(username, sql);
    }

    public static boolean doCheckEmailAvailability(String email) {
        final String sql = "SELECT email FROM company WHERE email = ?";

        return checkAvailability(email, sql);
    }

    public static boolean doCheckPhoneAvailability(String phone) {
        final String sql = "SELECT telephone FROM company WHERE telephone = ?";

        return checkAvailability(phone, sql);
    }

    public static boolean doCheckVATAvailability(String vat) {
        final String sql = "SELECT vat FROM company WHERE vat = ?";

        return checkAvailability(vat, sql);
    }

    public static boolean doCheckCompanyNameAvailability(String companyName) {
        final String sql = "SELECT company_name FROM company WHERE company_name = ?";

        return checkAvailability(companyName, sql);
    }

    // Metodi privati
    private static boolean checkAvailability(String str, String sql) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean res = false;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;

            ps = con.prepareStatement(sql);

            ps.setString(1, str);
            rs = ps.executeQuery();

            res = rs.next();
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return !res;
    }
}
