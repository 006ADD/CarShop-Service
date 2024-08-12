package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.AuditLog;
import model.User;
import repositories.AuditLogRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class AuditService {

    private AuditLogRepository auditLogRepository;

    public void logAction(User user, String action) {
        AuditLog log = new AuditLog(0, user, action, LocalDateTime.now()); // ID будет задан автоматически
        try {
            auditLogRepository.save(log);
        } catch (SQLException e) {
            e.printStackTrace();
            // Обработка ошибок
        }
    }

    public List<AuditLog> getLogs() {
        try {
            return auditLogRepository.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Возвращаем пустой список в случае ошибки
        }
    }

    public List<AuditLog> filterLogsByUser(User user) {
        try {
            return auditLogRepository.findByUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Возвращаем пустой список в случае ошибки
        }
    }

    public List<AuditLog> filterLogsByAction(String action) {
        try {
            return auditLogRepository.findByAction(action);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // Возвращаем пустой список в случае ошибки
        }
    }
}
