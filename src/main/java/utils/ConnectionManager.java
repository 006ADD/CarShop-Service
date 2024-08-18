package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String DRIVER = "org.postgresql.Driver";
    private static String jdbcUrl = "jdbc:postgresql://localhost:5432/carshopservice";
    private static String username = "userPostgres";
    private static String password = "passwordPostgres";

    private Connection conn;

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
//        if (conn == null || conn.isClosed()) {
//            try {
//                Class.forName(DRIVER); // Загрузка драйвера
//                conn = DriverManager.getConnection(jdbcUrl, username, password);
//            } catch (ClassNotFoundException e) {
//                throw new SQLException("Driver not found", e);
//            }
//        }
//        return conn;
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static String getJdbcUrl() {
        return jdbcUrl;
    }

    public static void setJdbcUrl(String jdbcUrl) {
        ConnectionManager.jdbcUrl = jdbcUrl;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ConnectionManager.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        ConnectionManager.password = password;
    }
}

