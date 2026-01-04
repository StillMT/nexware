package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.OrderedProductBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.enums.ProductStatus;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderedProductDAO {

    private static final Logger logger = Logger.getLogger(OrderedProductDAO.class.getName());

    public static List<OrderedProductBean> getByOrderId(int orderId) {

        final String sql =
                "SELECT op.id AS op_id, op.price, op.id_product, p.name, p.description, p.state " +
                        "FROM ordered_product op " +
                        "JOIN product p ON op.id_product = p.id " +
                        "WHERE op.id_order = ?";

        List<OrderedProductBean> items = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null) return items;

            ps = con.prepareStatement(sql);
            ps.setInt(1, orderId);

            rs = ps.executeQuery();
            while (rs.next()) {
                ProductBean product = new ProductBean();
                product.setId(rs.getInt("id_product"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));


                String rawState = rs.getString("state");
                if (rawState != null) {
                    product.setStatus(ProductStatus.fromString(rawState.trim().toUpperCase()));
                }

                OrderedProductBean item = new OrderedProductBean();
                item.setId(rs.getInt("op_id"));
                item.setOrderId(orderId);
                item.setProduct(product);
                item.setPrice(rs.getBigDecimal("price"));

                items.add(item);
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return items;
    }
}