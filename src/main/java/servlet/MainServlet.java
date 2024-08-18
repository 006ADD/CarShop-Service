package servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import services.*;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final CarService carService = new CarService();
    private final OrderService orderService = new OrderService();
    private final ServiceRequest serviceRequestService = new ServiceRequest();
    private final ClientService clientService = new ClientService();
    private final EmployeeService employeeService = new EmployeeService();
    private final AuditService auditService = new AuditService();
}
