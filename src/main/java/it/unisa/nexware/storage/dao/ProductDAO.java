package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.CompanyBean;
import it.unisa.nexware.application.beans.ProductBean;
import it.unisa.nexware.application.enums.ProductStatus;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDAO {

    private static final Logger logger = Logger.getLogger(ProductDAO.class.getName());

    public static List<ProductBean> doGetCheckoutProducts(CompanyBean company) {
        final String sql = "SELECT P.id, P.name, P.price, P.stock, C.company_name FROM carted_product CP " +
                "JOIN product P ON CP.id_product = P.id JOIN company C ON P.id_company = C.id " +
                "WHERE CP.id_company = ? AND P.state != 'HIDDEN' AND P.state != 'CANCELED' AND P.stock >= 1 ORDER BY CP.id";
        List<ProductBean> products = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return null;

            ps = con.prepareStatement(sql);
            ps.setInt(1, company.getId());

            rs = ps.executeQuery();
            while (rs.next())
                products.add(new ProductBean(rs.getInt("id"), rs.getString("name"),
                        rs.getBigDecimal("price"), rs.getInt("stock"), rs.getString("company_name")));
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return products;
    }

    public static List<ProductBean> doGetCartedProducts(CompanyBean company) {
        final String sql = "SELECT P.id, P.name, P.price, P.stock, P.state, C.company_name FROM carted_product CP " +
                "JOIN product P ON CP.id_product = P.id JOIN company C ON P.id_company = C.id " +
                "WHERE CP.id_company = ? ORDER BY CP.id";
        List<ProductBean> products = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return null;

            ps = con.prepareStatement(sql);
            ps.setInt(1, company.getId());

            rs = ps.executeQuery();
            while (rs.next())
                products.add(new ProductBean(rs.getInt("id"), rs.getString("name"),
                        ProductStatus.valueOf(rs.getString("state")), rs.getBigDecimal("price"),
                        rs.getInt("stock"), rs.getString("company_name")));
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return products;
    }

    public static List<ProductBean> doGetProductsByCompany(CompanyBean company, String searchQuery,
                                                           String startDate, String endDate, String statusFilter) {
        final String sql = "SELECT * FROM product WHERE id_company = ? AND !(state = 'CANCELED' AND modifying_date <= DATE_SUB(NOW(), INTERVAL 1 DAY))" +
                (searchQuery != null && !searchQuery.isBlank() ? " AND name LIKE ?" : "") + " AND creation_date >= ? AND creation_date <= ?" +
                (statusFilter.equals("ALL") ? "" : " AND state = ?") + " LIMIT 100";
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

    public static ProductBean doGetProductById(int id, boolean canceledStateTimeout) {
        final String sql = "SELECT P.id, P.name, P.description, P.id_category, P.id_company, P.state, P.price, P.stock, " +
                "C.company_name FROM product P INNER JOIN company C ON P.id_company = C.id WHERE P.id = ? AND " +
                (canceledStateTimeout ? "!(P.state = 'CANCELED' AND P.modifying_date <= DATE_SUB(NOW(), INTERVAL 1 DAY))" : "P.state != 'CANCELED'");
        ProductBean product = null;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return null;

            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            if (rs.next())
                product = new ProductBean(rs.getInt("id"), rs.getString("name"),
                        rs.getString("description"), rs.getInt("id_category"),
                        rs.getInt("id_company"), ProductStatus.valueOf(rs.getString("state")),
                        rs.getBigDecimal("price"), rs.getInt("stock"), rs.getString("company_name"));
            else
                product = new ProductBean();
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return product;
    }

    public static int doSaveProduct(ProductBean product) {
        final String sql = "INSERT INTO product VALUES (NULL, ?, ?, ?, ?, DEFAULT, DEFAULT, ?, ?, ?)";
        int result = 0;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return 0;

            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getIdCategory());
            ps.setInt(4, product.getIdCompany());
            ps.setString(5, product.getStatus().name());
            ps.setBigDecimal(6, product.getPrice());
            ps.setInt(7, product.getStock());

            if (ps.executeUpdate() == 1) {
                rs = ps.getGeneratedKeys();

                if (rs.next())
                    result = rs.getInt(1);
            }
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        }  finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return result;
    }

    public static boolean doEditProduct(ProductBean product) {
        final String sql = "UPDATE product SET name = ?, description = ?, id_category = ?, modifying_date = NOW()," +
                "state = ?, price = ?, stock = ? WHERE id = ?";
        boolean result = false;

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;

            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getIdCategory());
            ps.setString(4, product.getStatus().name());
            ps.setBigDecimal(5, product.getPrice());
            ps.setInt(6, product.getStock());
            ps.setInt(7, product.getId());

            if (ps.executeUpdate() == 1) {
                con.commit();
                result = true;
            } else
                con.rollback();

            con.setAutoCommit(true);
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        }  finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps);
        }

        return result;
    }

    public static boolean doCheckProductNameAvailability(String productName) {
        final String sql = "SELECT id FROM product WHERE name = ?";

        return CompanyDAO.checkAvailability(productName, sql);
    }

    public static List<ProductBean> doGetCatalogueProducts() {
        final String sql = "SELECT P.id, P.name, P.id_category, P.price, P.stock, C.company_name FROM product P INNER JOIN company C ON P.id_company = C.id WHERE P.state = 'ACTIVE' ORDER BY id DESC";

        List <ProductBean> products = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return products;

            ps= con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean product = (new ProductBean(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("id_category"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock")
                ));
                product.setCompanyName(rs.getString("company_name"));

                products.add(product);
            }

        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        }finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return products;
    }

    public static List<ProductBean> doGetProductsByCategory(int categoryId) {
        final String sql = "SELECT P.id, P.name, P.id_category, P.price, P.stock, C.company_name " +
                "FROM product P INNER JOIN company C ON P.id_company = C.id " +
                "WHERE P.state = 'ACTIVE' AND P.id_category = ? " +
                "ORDER BY P.id DESC";

        List<ProductBean> products = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null) return products;

            ps = con.prepareStatement(sql);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("id_category"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock")
                );
                product.setCompanyName(rs.getString("company_name"));
                products.add(product);
            }

        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return products;
    }

    public static List<ProductBean> doSearchByName(String name) {
        final String sql = "SELECT P.id, P.name, P.id_category, P.price, P.stock, C.company_name " +
                "FROM product P INNER JOIN company C ON P.id_company = C.id " +
                "WHERE P.state = 'ACTIVE' AND P.name LIKE ? " +
                "ORDER BY P.id DESC";

        List<ProductBean> products = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null) return products;

            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductBean product = new ProductBean(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("id_category"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock")
                );
                product.setCompanyName(rs.getString("company_name"));
                products.add(product);
            }

        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return products;
    }
}