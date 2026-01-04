package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.OrderBean;
import it.unisa.nexware.application.beans.OrderedProductBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.enums.OrderStatus;
import it.unisa.nexware.application.enums.ProductStatus;
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
                "SELECT o.*, op.id AS op_id, op.price AS purchase_price, " +
                        "p.id AS p_id, p.name AS p_name, p.description AS p_desc, p.state AS p_state " +
                        "FROM order_table o " +
                        "LEFT JOIN ordered_product op ON o.id = op.id_order " +
                        "LEFT JOIN product p ON op.id_product = p.id " +
                        "WHERE o.id_company = ? " +
                        (searchQuery != null && !searchQuery.isBlank() ? " AND o.order_nr LIKE ?" : "") +
                        " AND o.date >= ? AND o.date <= ? " +
                        (!statusFilter.equals("ALL") ? " AND o.state = ?" : "") +
                        " ORDER BY o.date DESC";

        Map<Integer, OrderBean> orderMap = new LinkedHashMap<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null) return new ArrayList<>();

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, company.getId());
            if (searchQuery != null && !searchQuery.isBlank()) ps.setString(i++, "%" + searchQuery.trim() + "%");
            ps.setString(i++, startDate);
            ps.setString(i++, endDate);
            if (!statusFilter.equals("ALL")) ps.setString(i, statusFilter);

            rs = ps.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                OrderBean order = orderMap.get(orderId);

                if (order == null) {
                    order = new OrderBean(
                            orderId,
                            rs.getInt("id_company"),
                            rs.getString("order_nr"),
                            OrderStatus.valueOf(rs.getString("state")),
                            rs.getDate("date").toLocalDate(),
                            rs.getString("half_card_number")
                    );
                    order.setTotalPrice(rs.getBigDecimal("total_price"));
                    order.setProducts(new ArrayList<>());
                    orderMap.put(orderId, order);
                }

                if (rs.getObject("op_id") != null) {
                    ProductBean p = new ProductBean();
                    p.setId(rs.getInt("p_id"));
                    p.setName(rs.getString("p_name"));
                    p.setDescription(rs.getString("p_desc"));
                    if (rs.getString("p_state") != null) {
                        p.setStatus(it.unisa.nexware.application.enums.ProductStatus.fromString(rs.getString("p_state").trim().toUpperCase()));
                    }

                    OrderedProductBean item = new OrderedProductBean();
                    item.setId(rs.getInt("op_id"));
                    item.setOrderId(orderId);
                    item.setProduct(p);
                    item.setPrice(rs.getBigDecimal("purchase_price"));

                    order.getProducts().add(item);
                }
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return new ArrayList<>(orderMap.values());
    }

    public static OrderBean getOrderByNumber(String orderNr, int idCompany) {
        final String sql =
                "SELECT o.*, op.id AS op_id, op.price AS purchase_price, " +
                        "p.id AS p_id, p.name AS p_name, p.description AS p_desc, p.state AS p_state " +
                        "FROM order_table o " +
                        "LEFT JOIN ordered_product op ON o.id = op.id_order " +
                        "LEFT JOIN product p ON op.id_product = p.id " +
                        "WHERE o.order_nr = ? AND o.id_company = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        OrderBean order = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, orderNr);
            ps.setInt(2, idCompany);
            rs = ps.executeQuery();

            while (rs.next()) {
                if (order == null) {
                    order = new OrderBean(
                            rs.getInt("id"),
                            rs.getInt("id_company"),
                            rs.getString("order_nr"),
                            it.unisa.nexware.application.enums.OrderStatus.valueOf(rs.getString("state")),
                            rs.getDate("date").toLocalDate(),
                            rs.getString("half_card_number")
                    );
                    order.setTotalPrice(rs.getBigDecimal("total_price"));
                    order.setProducts(new ArrayList<>());
                }

                int opId = rs.getInt("op_id");
                if (!rs.wasNull()) {
                    ProductBean product = new ProductBean();
                    product.setId(rs.getInt("p_id"));
                    product.setName(rs.getString("p_name"));
                    product.setDescription(rs.getString("p_desc"));

                    String rawState = rs.getString("p_state");
                    if (rawState != null) {
                        product.setStatus(ProductStatus.fromString(rawState.trim().toUpperCase()));
                    }

                    OrderedProductBean item = new OrderedProductBean();
                    item.setId(opId);
                    item.setOrderId(order.getId());
                    item.setProduct(product);
                    item.setPrice(rs.getBigDecimal("purchase_price"));

                    order.getProducts().add(item);
                }
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }
        return order;
    }

    public static boolean updateOrderStatus(String orderNr, int idCompany, OrderStatus newStatus) {

        final String sql = "UPDATE order_table SET state = ? WHERE id_company = ? AND order_nr = ? AND state != ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null) return false;

            ps = con.prepareStatement(sql);


            ps.setString(1, newStatus.name());

            ps.setInt(2, idCompany);

            ps.setString(3, orderNr);

            ps.setString(4, newStatus.name());

            int rowsUpdated = ps.executeUpdate();


            return rowsUpdated > 0;

        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
            return false;
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, null);
        }
    }

}
