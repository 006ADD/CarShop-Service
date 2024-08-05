package services;

import model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для управления сотрудниками.
 * <p>
 * Этот класс предоставляет методы для добавления, удаления, обновления и получения сотрудников.
 * Сотрудники хранятся в карте, где ключом является уникальный идентификатор сотрудника.
 * </p>
 */
public class EmployeeService {
    /**
     * Карта, хранящая сотрудников по их уникальным идентификаторам.
     */
    private Map<Integer, User> employees = new HashMap<>();

    /**
     * Добавляет нового сотрудника в сервис.
     * <p>
     * Если сотрудник с таким же идентификатором уже существует, он будет заменен.
     * </p>
     *
     * @param employee Сотрудник, которого необходимо добавить.
     */
    public void addEmployee(User employee) {
        employees.put(employee.getId(), employee);
    }

    /**
     * Удаляет сотрудника по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор сотрудника.
     */
    public void deleteEmployee(int id) {
        employees.remove(id);
    }

    /**
     * Выводит информацию о всех сотрудниках.
     * <p>
     * Этот метод печатает в консоль информацию о каждом сотруднике, хранящемся в сервисе.
     * </p>
     */
    public void getAllEmployees() {
        for (Map.Entry<Integer, User> entry : employees.entrySet()) {
            Integer id = entry.getKey();
            User user = entry.getValue();
            System.out.println("ID: " + id + ", User: " + user);
        }
    }

    /**
     * Получает сотрудника по его уникальному идентификатору.
     *
     * @param employeeId Уникальный идентификатор сотрудника.
     * @return Сотрудник с указанным идентификатором или {@code null}, если сотрудник не найден.
     */
    public User getUserById(int employeeId) {
        return employees.get(employeeId);
    }

    /**
     * Обновляет информацию о сотруднике.
     * <p>
     * Изменяет имя и контактную информацию сотрудника, если они указаны и не пусты.
     * Если сотрудника с указанным идентификатором не существует, метод ничего не делает.
     * </p>
     *
     * @param employeeId       Уникальный идентификатор сотрудника.
     * @param newName          Новое имя сотрудника.
     * @param newContactInfo   Новая контактная информация сотрудника.
     */
    public void updateEmployee(int employeeId, String newName, String newContactInfo) {
        User user = employees.get(employeeId);
        if (user != null) {
            if (!newName.isEmpty()) {
                user.setName(newName);
            }
            if (!newContactInfo.isEmpty()) {
                user.setContactInfo(newContactInfo);
            }
            employees.put(employeeId, user);  // Обновляем сотрудника в коллекции
        }
    }
}
