package repositories;

import model.Car;
import model.Order;
import model.User;
import services.CarService;
import services.UserService;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class OrderRepository {
    private final UserService userService;
    private final CarService carService;

    private final DataSource dataSource;


    public OrderRepository(UserService userService, CarService carService, DataSource dataSource) {
        this.userService = userService;
        this.carService = carService;
        this.dataSource = dataSource;
    }

    public void createOrder(Order order) {
        String SQL = "INSERT INTO orders (id, client_id, car_id, order_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, order.getId());
            prs.setInt(2, order.getClient().getId());  // Предполагается, что у клиента есть метод getId()
            prs.setInt(3, order.getCar().getId());     // Предполагается, что у автомобиля есть метод getId()
            prs.setDate(4, java.sql.Date.valueOf(order.getOrderDate()));  // Преобразование LocalDate в SQL Date
            prs.setString(5, order.getStatus());
            prs.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void updateOrderStatus(int id, String status) {
        String SQL = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setString(1, status);
            prs.setInt(2, id);
            prs.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }


    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String SQL = "SELECT * FROM orders";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL);
             ResultSet rs = prs.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int clientId = rs.getInt("client_id");
                int carId = rs.getInt("car_id");
                LocalDate orderDate = rs.getDate("order_date").toLocalDate();
                String status = rs.getString("status");

                User client = userService.getUserId(clientId);
                Car car = carService.getCar(carId);
                Order order = new Order(id, client, car, orderDate, status);
                orders.add(order);
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return orders;
    }

    public void remove(int id) {
        String SQL = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, id);
            prs.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public Order findById(int orderId) {
        String SQL = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement prs = conn.prepareStatement(SQL)) {
            prs.setInt(1, orderId);
            try (ResultSet rs = prs.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int clientId = rs.getInt("client_id");
                    int carId = rs.getInt("car_id");
                    LocalDate orderDate = rs.getDate("order_date").toLocalDate();
                    String status = rs.getString("status");

                    User client = userService.getUserId(clientId);
                    Car car = carService.getCar(carId);

                    return new Order(id, client, car, orderDate, status);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }
}
