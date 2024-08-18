package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.*;

import java.io.IOException;

@WebServlet("/carshopservice")
public class CarShopServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final CarService carService = new CarService();
    private final OrderService orderService = new OrderService();
    private final ServiceRequest serviceRequestService = new ServiceRequest();
    private final ClientService clientService = new ClientService();
    private final AuditService auditService = new AuditService();
    private final EmployeeService employeeService = new EmployeeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if("login".equals(action)){
            req.getRequestDispatcher("/login").forward(req,resp);
        }else if("main".equals(action)){
            req.getRequestDispatcher("/main").forward(req,resp);
        }else{
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
