package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CategoryBean;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CategoryDAO {

    public static List<CategoryBean> doGetCatList() {
        if (!categories.isEmpty())
            return categories;

        final String sql = "SELECT * FROM category";

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

        return "";
    }

    // Attributi
    private static final Logger logger = Logger.getLogger(CategoryDAO.class.getName());
    private static final List<CategoryBean> categories = new ArrayList<>();
}