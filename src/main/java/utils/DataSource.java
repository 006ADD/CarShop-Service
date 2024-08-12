package utils;

import lombok.*;

import java.sql.*;


public class DataSource {

    private static final String DRIVER = "org.postgresql.Driver";
    private static String jdbcUrl = "jdbc:postgresql://localhost:5432/carshopservice";
    private static String username = "userPostgres";
    private static String password = "passwordPostgres";

    private Connection conn;


    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static void setJdbcUrl(String jdbcUrl) {
        DataSource.jdbcUrl = jdbcUrl;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DataSource.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DataSource.password = password;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName(DRIVER); // Загрузка драйвера
                conn = DriverManager.getConnection(jdbcUrl, username, password);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver not found", e);
            }
        }
        return conn;
    }

    // Метод для закрытия соединения
    public void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try (Statement stmt = getConnection().createStatement()) {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
        return rs;
    }

    public int executeUpdate(String sql) {
        int rowsAffected = 0;
        try (Statement stmt = getConnection().createStatement()) {
            rowsAffected = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error executing update: " + e.getMessage());
        }
        return rowsAffected;
    }
}
