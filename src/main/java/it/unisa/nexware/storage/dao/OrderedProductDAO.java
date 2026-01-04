package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.OrderedProductBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderedProductDAO {

    private static final Logger logger = Logger.getLogger(OrderedProductDAO.class.getName());

    public static List<OrderedProductBean> getByOrderId(int orderId) {

        final String sql =
                "SELECT op.id, op.id_order, op.id_product, op.price, " +
                        "p.name, p.description " +
                        "FROM ordered_product op " +
                        "JOIN product p ON op.id_product = p.id " +
                        "WHERE op.id_order = ?";

        List<OrderedProductBean> items = new ArrayList<>();

        try (Connection con = DriverManagerConnectionPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    ProductBean product = new ProductBean();
                    product.setId(rs.getInt("id_product"));
                    product.setName(rs.getString("name"));
                    product.setDescription(rs.getString("description"));


                    OrderedProductBean item = new OrderedProductBean();
                    item.setId(rs.getInt("id"));
                    item.setOrderId(rs.getInt("id_order"));
                    item.setProduct(product);
                    item.setPrice(rs.getBigDecimal("price"));

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        }

        return items;
    }
}