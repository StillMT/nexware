package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.enums.ProductStatus;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDAO {

    // Attributi
    private static final Logger logger = Logger.getLogger(ProductDAO.class.getName());

    public static List<ProductBean> doGetProductsByCompany(CompanyBean company, String searchQuery,
                                                           String startDate, String endDate, String statusFilter) {
        final String sql = "SELECT * FROM product WHERE id_company = ?" +
                (searchQuery != null && !searchQuery.isBlank() ? " AND name LIKE ?" : "") +
                " AND creation_date >= ? AND creation_date <= ?" +
                (statusFilter.equals("ALL") ? "" : " AND state = ?");
        List<ProductBean> products = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return null;

            ps = con.prepareStatement(sql);

            int index = 1;
            ps.setInt(index++, company.getId());
            if (searchQuery != null && !searchQuery.isBlank())
                ps.setString(index++, "%" + searchQuery.trim() + "%");
            ps.setString(index++, startDate);
            ps.setString(index++, endDate);
            if (!statusFilter.equals("ALL"))
                ps.setString(index, statusFilter);

            rs = ps.executeQuery();
            while (rs.next())
                products.add(new ProductBean(rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getInt("id_category"),
                        rs.getTimestamp("creation_date").toLocalDateTime(), rs.getTimestamp("modifying_date").toLocalDateTime(),
                        ProductStatus.valueOf(rs.getString("state")), rs.getBigDecimal("price"), rs.getInt("stock")));

        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally{
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return products;
    }

}
