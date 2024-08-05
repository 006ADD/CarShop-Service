package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Представляет запись аудита для отслеживания действий пользователей.
 * <p>
 * Этот класс содержит информацию о действиях, которые выполняют пользователи, включая идентификатор записи,
 * пользователя, действие и временную метку.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {
    /**
     * Уникальный идентификатор записи аудита.
     */
    private int id;

    /**
     * Пользователь, который выполнил действие.
     */
    private User user;

    /**
     * Описание действия, выполненного пользователем.
     */
    private String action;

    /**
     * Временная метка, когда было выполнено действие.
     */
    private LocalDateTime timestamp;
}
