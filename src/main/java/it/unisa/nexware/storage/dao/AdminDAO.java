package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.AdminBean;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AdminDAO {

    public static AdminBean doLoginAdmin(String username, String password) {
        final String sql = "SELECT * FROM admin WHERE username = ?";
        AdminBean a = new AdminBean();

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

                if (BCrypt.checkpw(password, pwd))
                    a.setUsername(rs.getString("username"));
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return a;
    }

    // Attributi
    private static final Logger logger = Logger.getLogger(AdminDAO.class.getName());
}