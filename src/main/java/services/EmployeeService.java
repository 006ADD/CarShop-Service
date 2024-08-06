package services;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class EmployeeService {
    private Map<Integer, User> employees = new HashMap<>();

    public void addEmployee(User employee) {
        employees.put(employee.getId(), employee);
    }

    public void deleteEmployee(int id) {
        employees.remove(id);
    }

    public void getAllEmployees(){
        for (Map.Entry<Integer, User> entry : employees.entrySet()) {
            Integer id = entry.getKey();
            User user = entry.getValue();
            System.out.println("ID: " + id + ", User: " + user);
        }
    }

    public User getUserById(int employeeId) {
        return employees.get(employeeId);
    }

    public void updateEmployee(int employeeId, String newName, String newContactInfo) {
        User user = employees.get(employeeId);
        if (user != null) {
            if (!newName.isEmpty()) {
                user.setName(newName);
            }
            if (!newContactInfo.isEmpty()) {
                user.setContactInfo(newContactInfo);
            }
            employees.put(employeeId, user);  // Обновляем клиента в коллекции
        }
    }
}
