package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CategoryBean;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CategoryDAO {

    public static List<CategoryBean> doGetCatList() {
        if (!categories.isEmpty())
            return categories;

        final String sql = "SELECT * FROM category ORDER BY id";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return null;

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(new CategoryBean(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return categories;
    }

    public static String doGetCategoryById(int id) {
        if (categories.isEmpty())
            doGetCatList();

        for (CategoryBean c : categories)
            if (c.getId() == id)
                return c.getName();

        return null;
    }

    public static int doAddCategory(String catName) {
        final String sql = "INSERT IGNORE INTO category (name) VALUES (?);";
        int result = 0;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return 0;

            ps = con.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, catName);

            if (ps.executeUpdate() == 1) {
                rs = ps.getGeneratedKeys();
                rs.next();
                categories.add(new CategoryBean(result = rs.getInt(1), catName));
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return result;
    }

    public static boolean doRemoveCategory(int catId) {
        final String sql = "DELETE IGNORE category FROM category WHERE id = ?";
        boolean result = false;

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;

            ps = con.prepareStatement(sql);
            ps.setInt(1, catId);

            if (ps.executeUpdate() == 1) {
                result = true;

                for (CategoryBean c : categories)
                    if (c.getId() == catId) {
                        categories.remove(c);
                        break;
                    }
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps);
        }

        return result;
    }

    // Attributi
    private static final Logger logger = Logger.getLogger(CategoryDAO.class.getName());
    private static final List<CategoryBean> categories = new ArrayList<>();
}