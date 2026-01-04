package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.OrderBean;
import it.unisa.nexware.application.beans.OrderedProductBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.enums.OrderStatus;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

public class OrderDAO {

    private static final Logger logger = Logger.getLogger(OrderDAO.class.getName());

    public static boolean doCreateOrder(CompanyBean c, String orderNr, String cardNr,
                                        BigDecimal total, List<ProductBean> products) {
        String sql = "INSERT INTO order_table VALUES (NULL, ?, ?, DEFAULT, DEFAULT, ?, ?)";
        boolean result = false;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;
            con.setAutoCommit(false);

            ps = con.prepareStatement(sql,  Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, c.getId());
            ps.setString(2, orderNr);
            ps.setString(3, cardNr);
            ps.setBigDecimal(4, total);

            if (ps.executeUpdate() == 1) {
                rs = ps.getGeneratedKeys();
                rs.next();
                int orderId = rs.getInt(1);
                ps.close();

                sql = "INSERT INTO ordered_product VALUES (NULL, ?, ?, ?)";
                ps = con.prepareStatement(sql);

                for (ProductBean p : products) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, p.getId());
                    ps.setBigDecimal(3, p.getPrice());

                    ps.addBatch();
                }

                boolean batchSuccess = true;
                for (int r : ps.executeBatch())
                    if (r == Statement.EXECUTE_FAILED) {
                        batchSuccess = false;
                        break;
                    }
                if (batchSuccess) {
                    con.commit();
                    result = true;
                } else
                    con.rollback();

                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {DriverManagerConnectionPool.logSqlError(ex, logger);}

            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return result;
    }

    public static List<OrderBean> doGetOrdersByCompany(
            CompanyBean company,
            String searchQuery,
            String startDate,
            String endDate,
            String statusFilter) {

        final String sql =
                "SELECT * FROM order_table WHERE id_company = ?" +
                        (searchQuery != null && !searchQuery.isBlank() ? " AND order_nr LIKE ?" : "") +
                        " AND date >= ? AND date <= ?" +
                        (!statusFilter.equals("ALL") ? " AND state = ?" : "");

        List<OrderBean> orders = new ArrayList<>();

        try (Connection con = DriverManagerConnectionPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;
            ps.setInt(i++, company.getId());

            if (searchQuery != null && !searchQuery.isBlank()) {
                ps.setString(i++, "%" + searchQuery.trim() + "%");
            }

            ps.setString(i++, startDate);
            ps.setString(i++, endDate);

            if (!statusFilter.equals("ALL")) {
                ps.setString(i, statusFilter);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderBean order = new OrderBean(
                            rs.getInt("id"),
                            rs.getInt("id_company"),
                            rs.getString("order_nr"),
                            OrderStatus.valueOf(rs.getString("state")),
                            rs.getDate("date").toLocalDate(),
                            rs.getString("half_card_number")
                    );


                    BigDecimal total = BigDecimal.ZERO;
                    List<OrderedProductBean> items = OrderedProductDAO.getByOrderId(order.getId());
                    for (OrderedProductBean item : items) {
                        if (item.getPrice() != null) total = total.add(item.getPrice());
                    }
                    order.setTotalPrice(total);

                    orders.add(order);
                }
            }

        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        }

        return orders;
    }

    public static OrderBean getOrderById(int orderId, int companyId) {
        final String sql = "SELECT * FROM order_table WHERE id = ? AND id_company = ?";
        OrderBean order = null;

        try (Connection con = DriverManagerConnectionPool.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, companyId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = new OrderBean(
                            rs.getInt("id"),
                            rs.getInt("id_company"),
                            rs.getString("order_nr"),
                            OrderStatus.valueOf(rs.getString("state")),
                            rs.getDate("date").toLocalDate(),
                            rs.getString("half_card_number")
                    );


                    BigDecimal total = BigDecimal.ZERO;
                    List<OrderedProductBean> items = OrderedProductDAO.getByOrderId(orderId);
                    for (OrderedProductBean item : items) {
                        if (item.getPrice() != null) total = total.add(item.getPrice());
                    }
                    order.setTotalPrice(total);
                }
            }

        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        }

        return order;
    }
}