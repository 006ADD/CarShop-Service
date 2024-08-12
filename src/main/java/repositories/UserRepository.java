package repositories;


import model.User;
import utils.DataSource;

import java.sql.*;
import java.util.*;

public class UserRepository {
    private final DataSource dataSource = new DataSource();
    public void register(User user) {
        String SQL = "INSERT INTO users (name, password, role) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {

            prs.setString(1, user.getName());
            prs.setString(2, user.getPassword());  // Рекомендуется использовать хэширование паролей
            prs.setString(3, user.getRole());

            prs.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public User login(String name, String password) {
        String SQL = "SELECT * FROM users WHERE name = ? AND password = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {

            prs.setString(1, name);
            prs.setString(2, password);

            try (ResultSet rs = prs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String role = rs.getString("role");
                    return new User(id, name, password, role);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }


    public User findById(int id) {
        User user = null;
        String SQL = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, id);

            try (ResultSet rs = prs.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String name = rs.getString("name");
                    String password = rs.getString("password");
                    String role = rs.getString("role");

                    user = new User(userId, name, password, role);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        return user;
    }

    public Collection<User> findAll() {
        List<User> users = new ArrayList<>();
        String SQL = "SELECT * FROM users";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL);
             ResultSet rs = prs.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String role = rs.getString("role");
                String contactInfo = rs.getString("contactInfo");
                String password = rs.getString("password");  // Рекомендуется использовать хэширование паролей


                User user = new User(id, name, role, contactInfo,password);
                users.add(user);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return users;
    }

    public void remove(int id) {
        String SQL = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {

            prs.setInt(1, id);
            int rowsAffected = prs.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
