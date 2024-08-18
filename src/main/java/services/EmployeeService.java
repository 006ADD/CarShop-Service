package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.User;
import repositories.EmployeeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class EmployeeService {
    private  EmployeeRepository employeeRepository;


    public void addEmployee(User employee) {
        employeeRepository.add(employee);
    }

    public void deleteEmployee(int id) {
        employeeRepository.delete(id);
    }

    public List<User> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public User getUserById(int employeeId) {
        return employeeRepository.getUserById(employeeId);
    }

    public void updateEmployee(int employeeId, String newName, String newContactInfo) {
        employeeRepository.update(employeeId,newName,newContactInfo);
    }
}
