package repositories;

import lombok.AllArgsConstructor;
import model.SerRequest;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class ServiceRequestRepository {
    private final DataSource dataSource; // Класс для управления соединением с базой данных


    public void create(SerRequest request) {
        String SQL = "INSERT INTO service_requests (id, description, status) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, request.getId());
            prs.setString(2, request.getDescription());
            prs.setString(3, request.getStatus());
            prs.executeUpdate();
            System.out.println("Service request created successfully");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void update(int id, String status) {
        String SQL = "UPDATE service_requests SET status = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setString(1, status);
            prs.setInt(2, id);
            prs.executeUpdate();
            System.out.println("Service request status updated successfully");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public SerRequest findById(int id) {
        SerRequest serRequest = null;
        String SQL = "SELECT * FROM service_requests WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, id);
            try (ResultSet rs = prs.executeQuery()) {
                if (rs.next()) {
                    int requestId = rs.getInt("id");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    serRequest = new SerRequest(requestId, description, status);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return serRequest;
    }

    public Collection<SerRequest> findAll() {
        List<SerRequest> requests = new ArrayList<>();
        String SQL = "SELECT * FROM service_requests";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL);
             ResultSet rs = prs.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String status = rs.getString("status");
                requests.add(new SerRequest(id, description, status));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return requests;
    }
}
