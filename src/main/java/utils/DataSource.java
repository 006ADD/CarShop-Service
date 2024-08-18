package utils;

import java.sql.*;

public class DataSource {


    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
        return rs;
    }

    public int executeUpdate(String sql) {
        int rowsAffected = 0;
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            rowsAffected = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error executing update: " + e.getMessage());
        }
        return rowsAffected;
    }
}
