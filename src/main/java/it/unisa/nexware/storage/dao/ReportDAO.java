package it.unisa.nexware.storage.dao;

import it.unisa.nexware.application.beans.ReportBean;
import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReportDAO {

    // Attributi
    private static final Logger logger = Logger.getLogger(ReportDAO.class.getName());

    public static boolean doInsertReport(ReportBean r) {
        final String sql = "INSERT INTO report VALUES (NULL, ?, ?, ?, ?)";
        boolean result = false;

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return false;

            ps = con.prepareStatement(sql);

            ps.setString(1, r.getCompanyName());
            ps.setString(2, r.getEmail());
            ps.setString(3, r.getObject());
            ps.setString(4, r.getDescription());

            if (ps.executeUpdate() == 1)
                result = true;
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps);
        }

        return result;
    }

    public static List<ReportBean> doGetReports() {
        final String sql = "SELECT * FROM report ORDER BY id DESC LIMIT 100";
        List<ReportBean> reports = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DriverManagerConnectionPool.getConnection();
            if (con == null)
                return null;

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();
            while (rs.next())
                reports.add(new ReportBean(rs.getInt("id"), rs.getString("company_name"),
                        rs.getString("email"), rs.getString("object"),
                        rs.getString("description")));
        } catch (SQLException e) {
            DriverManagerConnectionPool.logSqlError(e, logger);
        } finally {
            DriverManagerConnectionPool.closeSqlParams(con, ps, rs);
        }

        return reports;
    }
}
