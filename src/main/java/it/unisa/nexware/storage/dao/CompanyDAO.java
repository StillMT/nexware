package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.enums.AccountStatus;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.logging.Logger;

public class CompanyDAO {

    // Attributi
    private static final Logger logger = Logger.getLogger(CompanyDAO.class.getName());

    // Metodi pubblici
    public static CompanyBean doLoginCompany(String username, String password) {
        final String sql = "SELECT * FROM company WHERE username = ?";
        CompanyBean company = null;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return null;

            ps = con.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();
            if (rs.next()) {
                String pwd = rs.getString("password_hash");

                if (BCrypt.checkpw(password, pwd)) {
                    company = new CompanyBean(rs.getInt("id"), rs.getString("username"),
                            rs.getString("email"), rs.getString("telephone"),
                            rs.getString("vat"), rs.getString("company_name"),
                            rs.getString("registered_office"),
                            AccountStatus.valueOf(rs.getString("status")));
                } else
                    company = new CompanyBean();
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return company;
    }

    public static int doRegisterCompany(CompanyBean company, String password) {
        final String sql = "INSERT INTO company VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, DEFAULT, ?)";
        int result = 0;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, company.getUsername());
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            ps.setString(3, company.getEmail());
            ps.setString(4, company.getTelephone());
            ps.setString(5, company.getVat());
            ps.setString(6, company.getCompanyName());
            ps.setString(7, company.getCompanyAddress());
            ps.setString(8, company.getStatus().name());

            if (ps.executeUpdate() == 1) {
                rs = ps.getGeneratedKeys();

                if (rs.next())
                    result = rs.getInt(1);
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return result;
    }

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

    public static boolean checkAvailability(String str, String sql) {
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
