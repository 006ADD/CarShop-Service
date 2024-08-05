package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет пользователя системы.
 * <p>
 * Этот класс содержит информацию о пользователе, включая идентификатор, имя, роль, контактную информацию и пароль.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * Уникальный идентификатор пользователя.
     */
    private int id;

    /**
     * Имя пользователя.
     */
    private String name;

    /**
     * Роль пользователя в системе.
     * <p>
     * Роль может быть одной из следующих: "admin", "manager", "client".
     * </p>
     */
    private String role;

    /**
     * Контактная информация пользователя.
     */
    private String contactInfo;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Конструктор для создания объекта пользователя с минимальным набором данных.
     * <p>
     * Этот конструктор используется, когда необходимо создать объект пользователя, указывая только идентификатор,
     * имя и контактную информацию.
     * </p>
     *
     * @param id          Уникальный идентификатор пользователя.
     * @param name        Имя пользователя.
     * @param contactInfo Контактная информация пользователя.
     */
    public User(int id, String name, String contactInfo) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
    }
}
