package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.OrderBean;
import it.unisa.nexware.application.enums.OrderStatus;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class OrderDAO {
    private static final Logger logger = Logger.getLogger(OrderDAO.class.getName());

    public static List<OrderBean> doGetOrdersByCompany(CompanyBean company, String searchQuery, String startDate, String endDate, String statusFilter) {
        StringBuilder sql = new StringBuilder("SELECT * FROM order_table WHERE id_company = ? ");
        if (searchQuery != null && !searchQuery.isBlank()) sql.append("AND order_nr LIKE ? ");
        sql.append("AND date >= ? AND date <= ? ");
        if (statusFilter != null && !statusFilter.equals("ALL")) sql.append("AND state = ? ");

        List<OrderBean> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null) return orders;
            ps = con.prepareStatement(sql.toString());

            int i = 1;
            ps.setInt(i++, company.getId());
            if (searchQuery != null && !searchQuery.isBlank()) ps.setString(i++, "%" + searchQuery.trim() + "%");
            ps.setString(i++, startDate);
            ps.setString(i++, endDate);
            if (statusFilter != null && !statusFilter.equals("ALL")) ps.setString(i, statusFilter);

            rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(new OrderBean(
                        rs.getInt("id"),
                        rs.getInt("id_company"),
                        rs.getString("order_nr"),
                        rs.getBigDecimal("total_price"),
                        OrderStatus.fromString(rs.getString("state")),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("half_card_number")
                ));
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }
        return orders;
    }

    public static OrderBean getOrderByNumber(String orderNr, int companyId) {
        final String sql = "SELECT * FROM order_table WHERE order_nr = ? AND id_company = ?";
        OrderBean order = null;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null) return null;

            ps = con.prepareStatement(sql);
            ps.setString(1, orderNr);
            ps.setInt(2, companyId);

            rs = ps.executeQuery();
            if (rs.next()) {
                order = new OrderBean(
                        rs.getInt("id"),
                        rs.getInt("id_company"),
                        rs.getString("order_nr"),
                        rs.getBigDecimal("total_price"),
                        OrderStatus.fromString(rs.getString("state")),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("half_card_number")
                );
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }
        return order;
    }

    public static boolean updateOrderStatus(String orderNr, OrderStatus newStatus) {
        final String sql = "UPDATE order_table SET state = ? WHERE order_nr = ?";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null) return false;

            ps = con.prepareStatement(sql);
            ps.setString(1, newStatus.name());
            ps.setString(2, orderNr);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
            return false;
        } finally {

            DriverManagerConnectionPool.closeSqlParams(con, ps, null);
        }
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