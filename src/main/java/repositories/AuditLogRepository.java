package repositories;

import model.AuditLog;
import model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditLogRepository {
    private final Connection connection;
    public AuditLogRepository(Connection connection) {
        this.connection = connection;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/carshopservice";
        String username = "userPostgres";
        String password = "passwordPostgres";
        return DriverManager.getConnection(url, username, password);
    }

    public void save(AuditLog log) throws SQLException {
        String query = "INSERT INTO audit_log (user_id, action, timestamp) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, log.getUser().getId());
            statement.setString(2, log.getAction());
            statement.setTimestamp(3, Timestamp.valueOf(log.getTimestamp()));
            statement.executeUpdate();
        }
    }

    public List<AuditLog> findAll() throws SQLException {
        List<AuditLog> logs = new ArrayList<>();
        String query = "SELECT * FROM audit_log";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                AuditLog log = mapRowToAuditLog(resultSet);
                logs.add(log);
            }
        }
        return logs;
    }

    public List<AuditLog> findByUser(User user) throws SQLException {
        List<AuditLog> logs = new ArrayList<>();
        String query = "SELECT * FROM audit_log WHERE user_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AuditLog log = mapRowToAuditLog(resultSet);
                    logs.add(log);
                }
            }
        }
        return logs;
    }

    public List<AuditLog> findByAction(String action) throws SQLException {
        List<AuditLog> logs = new ArrayList<>();
        String query = "SELECT * FROM audit_log WHERE action = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, action);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AuditLog log = mapRowToAuditLog(resultSet);
                    logs.add(log);
                }
            }
        }
        return logs;
    }

    private AuditLog mapRowToAuditLog(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int userId = resultSet.getInt("user_id");
        String action = resultSet.getString("action");
        LocalDateTime timestamp = resultSet.getTimestamp("timestamp").toLocalDateTime();
        // Создайте объект User на основе userId, если это необходимо
        User user = new User(userId, "", "", "");
        return new AuditLog(id, user, action, timestamp);
    }
}
