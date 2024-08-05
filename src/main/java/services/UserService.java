package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для управления пользователями.
 * <p>
 * Этот класс предоставляет методы для регистрации, входа, удаления и получения пользователей.
 * Также предоставляет методы для проверки ролей пользователей.
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
public class UserService {

    /**
     * Хранит пользователей в памяти.
     */
    private Map<String, User> users = new HashMap<>();

    /**
     * Регистрирует нового пользователя.
     * <p>
     * Добавляет пользователя в коллекцию, если пользователь с таким именем еще не зарегистрирован.
     * </p>
     *
     * @param user Пользователь, который должен быть зарегистрирован.
     */
    public void registerUser(User user) {
        if (!users.containsKey(user.getName())) {
            users.put(user.getName(), user);
        } else {
            System.out.println("Пользователь уже существует.");
        }
    }

    /**
     * Выполняет вход пользователя.
     * <p>
     * Проверяет, существует ли пользователь с данным именем и совпадает ли пароль.
     * </p>
     *
     * @param name     Имя пользователя.
     * @param password Пароль пользователя.
     * @return Пользователь, если имя и пароль совпадают; {@code null} в противном случае.
     */
    public User loginUser(String name, String password) {
        User user = users.get(name);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * Получает всех зарегистрированных пользователей.
     *
     * @return Коллекция всех пользователей.
     */
    public Collection<User> getAllUsers() {
        return users.values();
    }

    /**
     * Удаляет пользователя по его идентификатору.
     * <p>
     * Этот метод предполагает, что идентификатор пользователя используется в качестве ключа в хранилище.
     * </p>
     *
     * @param id Идентификатор пользователя, который должен быть удален.
     */
    public void deleteUser(int id) {
        users.remove(id);
    }

    /**
     * Проверяет, является ли пользователь администратором.
     *
     * @param user Пользователь, чья роль проверяется.
     * @return {@code true}, если пользователь является администратором; {@code false} в противном случае.
     */
    public boolean isAdmin(User user) {
        return "admin".equalsIgnoreCase(user.getRole());
    }

    /**
     * Проверяет, является ли пользователь менеджером.
     *
     * @param user Пользователь, чья роль проверяется.
     * @return {@code true}, если пользователь является менеджером; {@code false} в противном случае.
     */
    public boolean isManager(User user) {
        return "manager".equalsIgnoreCase(user.getRole());
    }

    /**
     * Проверяет, является ли пользователь клиентом.
     *
     * @param user Пользователь, чья роль проверяется.
     * @return {@code true}, если пользователь является клиентом; {@code false} в противном случае.
     */
    public boolean isClient(User user) {
        return "client".equalsIgnoreCase(user.getRole());
    }
}
