package repositories;

import model.User;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class ClientRepository {
    private final DataSource dataSource;

    public ClientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void add(User client) {
        String SQL = "INSERT INTO clients (id, name, contact_info) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, client.getId());
            prs.setString(2, client.getName());
            prs.setString(3, client.getContactInfo());
            prs.executeUpdate();
            System.out.println("Client added successfully");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void update(int id, String newName, String newContactInfo) {
        String SQL = "UPDATE clients SET name = ?, contact_info = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setString(1, newName);
            prs.setString(2, newContactInfo);
            prs.setInt(3, id);
            prs.executeUpdate();
            System.out.println("Client updated successfully");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public User getUserById(int clientId) {
        User user = null;
        String SQL = "SELECT * FROM clients WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, clientId);
            try (ResultSet rs = prs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String contactInfo = rs.getString("contact_info");
                    user = new User(id, name, "", contactInfo); // Актуализируйте конструктор User по необходимости
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return user;
    }

    public void delete(int id) {
        String SQL = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, id);
            prs.executeUpdate();
            System.out.println("Client deleted successfully");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public Collection<User> findAll() {
        List<User> clients = new ArrayList<>();
        String SQL = "SELECT * FROM clients";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL);
             ResultSet rs = prs.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String contactInfo = rs.getString("contact_info");
                clients.add(new User(id, name, "", contactInfo)); // Актуализируйте конструктор User по необходимости
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return clients;
    }
}
