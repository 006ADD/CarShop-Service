package servlet;

import model.Car;
import model.Order;
import model.User;
import services.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final CarService carService = new CarService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "viewAll":
                viewAllOrders(request, response);
                break;
            case "placeOrder":
                showPlaceOrderForm(request, response);
                break;
            case "updateStatus":
                showUpdateStatusForm(request, response);
                break;
            case "cancel":
                showCancelOrderForm(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "placeOrder":
                placeOrder(request, response);
                break;
            case "updateStatus":
                updateOrderStatus(request, response);
                break;
            case "cancel":
                cancelOrder(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    private void viewAllOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Order> orders = orderService.getAllOrders();
            if (orders.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No orders found");
                return;
            }
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/viewAllOrders.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving orders");
        }
    }

    private void showPlaceOrderForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Car> availableCars = carService.getAllCars();
            if (availableCars.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No cars available");
                return;
            }
            request.setAttribute("cars", availableCars);
            request.getRequestDispatcher("/placeOrderForm.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving car list");
        }
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int carNumber = Integer.parseInt(request.getParameter("carNumber"));

            List<Car> availableCars = carService.getAllCars();
            if (carNumber < 1 || carNumber > availableCars.size()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid car number");
                return;
            }

            Car selectedCar = availableCars.get(carNumber - 1);
            if (!"available".equalsIgnoreCase(selectedCar.getStatus())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Selected car is not available for purchase");
                return;
            }

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                response.sendRedirect("login");
                return;
            }

            Order order = new Order(orderService.getAllOrders().size() + 1, currentUser, selectedCar, LocalDate.now(), "in progress");
            orderService.createOrder(order);
            selectedCar.setStatus("sold");

            auditService.logAction(currentUser, "Placed an order for car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
            response.sendRedirect("order?action=viewAll");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid car number format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error placing order");
        }
    }

    private void showUpdateStatusForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("/updateOrderStatusForm.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error showing update status form");
        }
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");

            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }

            order.setStatus(status);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "Updated status of order: " + orderId + " to " + status);
            }

            response.sendRedirect("order?action=viewAll");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating order status");
        }
    }

    private void showCancelOrderForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("/cancelOrderForm.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error showing cancel order form");
        }
    }

    private void cancelOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));

            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }

            orderService.cancelOrder(orderId);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "Cancelled order: " + orderId);
            }

            response.sendRedirect("order?action=viewAll");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error cancelling order");
        }
    }
}
