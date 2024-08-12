package services;

import model.AuditLog;
import model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuditService {
    private List<AuditLog> auditLogs = new ArrayList<>();

    public void logAction(User user, String action) {
        AuditLog log = new AuditLog(auditLogs.size() + 1, user, action, LocalDateTime.now());
        auditLogs.add(log);
    }

    public List<AuditLog> getLogs() {
        return auditLogs;
    }

    public List<AuditLog> filterLogsByUser(User user) {
        return auditLogs.stream()
                .filter(log -> log.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public List<AuditLog> filterLogsByAction(String action) {
        return auditLogs.stream()
                .filter(log -> log.getAction().equalsIgnoreCase(action))
                .collect(Collectors.toList());
    }

}
