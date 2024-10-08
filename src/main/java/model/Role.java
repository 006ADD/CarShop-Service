package model;

/**
 * Перечисление ролей пользователей системы.
 * <p>
 * Это перечисление содержит различные роли, которые могут быть назначены пользователям системы.
 * Каждая роль определяет уровень доступа или привилегии пользователя в системе.
 * </p>
 */
public enum Role {
    /**
     * Роль администратора.
     * <p>
     * Пользователи с этой ролью имеют полный доступ к системе и могут управлять всеми аспектами.
     * </p>
     */
    ADMIN,

    /**
     * Роль менеджера.
     * <p>
     * Пользователи с этой ролью имеют доступ к управлению определенными функциональностями системы,
     * но не имеют полного доступа, как администраторы.
     * </p>
     */
    MANAGER,

    /**
     * Роль клиента.
     * <p>
     * Пользователи с этой ролью имеют ограниченный доступ и могут взаимодействовать с системой
     * в рамках своих клиентских функций.
     * </p>
     */
    CLIENT
}

