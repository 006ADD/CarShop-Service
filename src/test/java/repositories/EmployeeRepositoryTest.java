package repositories;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import utils.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14.3")
            .withDatabaseName("carshopservice")
            .withUsername("userPostgres")
            .withPassword("passwordPostgres");

    private DataSource dataSource;
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() throws Exception {
        postgresContainer.start();

        dataSource = new DataSource();
        dataSource.setJdbcUrl(postgresContainer.getJdbcUrl());
        dataSource.setUsername(postgresContainer.getUsername());
        dataSource.setPassword(postgresContainer.getPassword());

        employeeRepository = new EmployeeRepository(dataSource);

        try (var conn = dataSource.getConnection(); var stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE employees (id SERIAL PRIMARY KEY, name VARCHAR(255), contact_info VARCHAR(255))");
        }
    }

    @AfterEach
    public void tearDown() {
        postgresContainer.stop();
    }

    @Test
    public void testAddAndGetEmployee() {
        User employee = new User(1, "John Doe", "", "123-456-7890");
        employeeRepository.add(employee);

        User retrievedEmployee = employeeRepository.getUserById(1);
        assertNotNull(retrievedEmployee);
        assertEquals("John Doe", retrievedEmployee.getName());
        assertEquals("123-456-7890", retrievedEmployee.getContactInfo());
    }

    @Test
    public void testUpdateEmployee() {
        User employee = new User(1, "John Doe", "", "123-456-7890");
        employeeRepository.add(employee);

        employeeRepository.update(1, "Jane Doe", "987-654-3210");
        User updatedEmployee = employeeRepository.getUserById(1);
        assertNotNull(updatedEmployee);
        assertEquals("Jane Doe", updatedEmployee.getName());
        assertEquals("987-654-3210", updatedEmployee.getContactInfo());
    }

    @Test
    public void testDeleteEmployee() {
        User employee = new User(1, "John Doe", "", "123-456-7890");
        employeeRepository.add(employee);

        employeeRepository.delete(1);
        User deletedEmployee = employeeRepository.getUserById(1);
        assertNull(deletedEmployee);
    }

    @Test
    public void testFindAllEmployees() {
        User employee1 = new User(1, "John Doe", "", "123-456-7890");
        User employee2 = new User(2, "Jane Doe", "", "987-654-3210");
        employeeRepository.add(employee1);
        employeeRepository.add(employee2);

        List<User> employees = employeeRepository.findAll();
        assertEquals(2, employees.size());
    }
}
