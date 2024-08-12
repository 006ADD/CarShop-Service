package services;

import model.AuditLog;
import model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления записями аудита.
 * <p>
 * Этот класс предоставляет методы для записи действий пользователей и фильтрации записей аудита по пользователям
 * и действиям. Он хранит список всех записей аудита и позволяет получать и фильтровать эти записи.
 * </p>
 */
public class AuditService {
    /**
     * Список всех записей аудита.
     */
    private List<AuditLog> auditLogs = new ArrayList<>();

    /**
     * Записывает действие пользователя в журнал аудита.
     * <p>
     * Создает новую запись аудита с текущим временем и добавляет ее в список записей.
     * </p>
     *
     * @param user  Пользователь, который выполнил действие.
     * @param action Описание действия, выполненного пользователем.
     */
    public void logAction(User user, String action) {
        AuditLog log = new AuditLog(auditLogs.size() + 1, user, action, LocalDateTime.now());
        auditLogs.add(log);
    }

    /**
     * Получает все записи аудита.
     *
     * @return Список всех записей аудита.
     */
    public List<AuditLog> getLogs() {
        return auditLogs;
    }

    /**
     * Фильтрует записи аудита по пользователю.
     * <p>
     * Возвращает список записей, связанных с указанным пользователем.
     * </p>
     *
     * @param user Пользователь, по которому необходимо отфильтровать записи.
     * @return Список записей аудита для указанного пользователя.
     */
    public List<AuditLog> filterLogsByUser(User user) {
        return auditLogs.stream()
                .filter(log -> log.getUser().equals(user))
                .collect(Collectors.toList());
    }

    /**
     * Фильтрует записи аудита по действию.
     * <p>
     * Возвращает список записей, где действие соответствует указанному значению.
     * Сравнение производится без учета регистра.
     * </p>
     *
     * @param action Описание действия, по которому необходимо отфильтровать записи.
     * @return Список записей аудита для указанного действия.
     */
    public List<AuditLog> filterLogsByAction(String action) {
        return auditLogs.stream()
                .filter(log -> log.getAction().equalsIgnoreCase(action))
                .collect(Collectors.toList());
    }
}
