package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class CartDAO {

    // Attributi
    private static final Logger logger = Logger.getLogger(CartDAO.class.getName());

    public static boolean doAddProduct(int pId, CompanyBean c) {
        final String sql = "INSERT IGNORE INTO carted_product (id, id_company, id_product) " +
                "SELECT NULL, ?, ? FROM product P WHERE P.id = ? AND P.state = 'ACTIVE' AND P.id_company <> ?";
        boolean result = false;

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;

            ps = con.prepareStatement(sql);

            ps.setInt(1, c.getId());
            ps.setInt(2, pId);
            ps.setInt(3, pId);
            ps.setInt(4, c.getId());

            if (ps.executeUpdate() == 1)
                result = true;
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps);
        }

        return result;
    }

    public static boolean doRemoveProduct(int pId, CompanyBean c) {
        final String sql = "DELETE FROM carted_product WHERE id_company = ? AND id_product = ?";
        boolean result = false;

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;

            ps = con.prepareStatement(sql);

            ps.setInt(1, c.getId());
            ps.setInt(2, pId);

            if (ps.executeUpdate() == 1)
                result = true;
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        }  finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps);
        }

        return result;
    }

}
