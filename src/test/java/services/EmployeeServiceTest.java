package services;

import model.User;
import services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeServiceTest {

    private EmployeeService employeeService;

//    @BeforeEach
//    public void setUp() {
//        employeeService = new EmployeeService();
//    }
//
//    @Test
//    public void shouldAddEmployee() {
//        User employee = new User(1, "Alice", "alice@example.com");
//        employeeService.addEmployee(employee);
//
//        User retrievedEmployee = employeeService.getUserById(1);
//        assertThat(retrievedEmployee).isNotNull();
//        assertThat(retrievedEmployee.getName()).isEqualTo("Alice");
//        assertThat(retrievedEmployee.getContactInfo()).isEqualTo("alice@example.com");
//    }
//
//    @Test
//    public void shouldUpdateEmployee() {
//        User employee = new User(1, "Alice", "alice@example.com");
//        employeeService.addEmployee(employee);
//
//        employeeService.updateEmployee(1, "Bob", "bob@example.com");
//
//        User updatedEmployee = employeeService.getUserById(1);
//        assertThat(updatedEmployee).isNotNull();
//        assertThat(updatedEmployee.getName()).isEqualTo("Bob");
//        assertThat(updatedEmployee.getContactInfo()).isEqualTo("bob@example.com");
//    }
//
//    @Test
//    public void shouldDeleteEmployee() {
//        User employee = new User(1, "Alice", "alice@example.com");
//        employeeService.addEmployee(employee);
//        employeeService.deleteEmployee(1);
//
//        User deletedEmployee = employeeService.getUserById(1);
//        assertThat(deletedEmployee).isNull();
//    }
//
//    @Test
//    public void shouldGetEmployeeById() {
//        User employee = new User(1, "Alice", "alice@example.com");
//        employeeService.addEmployee(employee);
//
//        User retrievedEmployee = employeeService.getUserById(1);
//        assertThat(retrievedEmployee).isNotNull();
//        assertThat(retrievedEmployee.getId()).isEqualTo(1);
//        assertThat(retrievedEmployee.getName()).isEqualTo("Alice");
//        assertThat(retrievedEmployee.getContactInfo()).isEqualTo("alice@example.com");
//    }
}
