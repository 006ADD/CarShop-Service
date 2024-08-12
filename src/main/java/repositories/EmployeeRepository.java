package repositories;

import lombok.AllArgsConstructor;
import model.User;
import utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EmployeeRepository {
    private final DataSource dataSource;

    public EmployeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User employee) {
        String SQL = "INSERT INTO employees (id, name, contact_info) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, employee.getId());
            prs.setString(2, employee.getName());
            prs.setString(3, employee.getContactInfo());
            prs.executeUpdate();
            System.out.println("Employee added successfully");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void delete(int id) {
        String SQL = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, id);
            prs.executeUpdate();
            System.out.println("Employee deleted successfully");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String SQL = "SELECT * FROM employees";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL);
             ResultSet rs = prs.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String contactInfo = rs.getString("contact_info");
                User user = new User(id, name, "", contactInfo);
                users.add(user);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return users;
    }

    public User getUserById(int employeeId) {
        User user = null;
        String SQL = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, employeeId);
            try (ResultSet rs = prs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String contactInfo = rs.getString("contact_info");
                    user = new User(id, name, "", contactInfo); // Используйте актуальный конструктор User
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return user;
    }

    public void update(int employeeId, String newName, String newContactInfo) {
        String SQL = "UPDATE employees SET name = ?, contact_info = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setString(1, newName);
            prs.setString(2, newContactInfo);
            prs.setInt(3, employeeId);
            prs.executeUpdate();
            System.out.println("Employee updated successfully");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
