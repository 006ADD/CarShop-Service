package servlet;
import model.User;
import services.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    private final EmployeeService employeeService = new EmployeeService();
    private final UserService userService = new UserService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "viewAll":
                viewAllEmployees(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                showDeleteForm(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addNewEmployee(request, response);
                break;
            case "edit":
                editEmployeeInfo(request, response);
                break;
            case "delete":
                removeEmployee(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    private void viewAllEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("employees", employeeService.getAllEmployees());
            request.getRequestDispatcher("/viewAllEmployees.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving employee list");
        }
    }

    private void addNewEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String contactInfo = request.getParameter("contactInfo");

        try {
            User employee = new User(userService.getAllUsers().size() + 1, username, "manager", contactInfo, password);
            employeeService.addEmployee(employee);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "A new employee has been added: " + username);
            }

            response.sendRedirect("employee?action=viewAll");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding new employee");
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId;
        try {
            employeeId = Integer.parseInt(request.getParameter("employeeId"));
            User employee = employeeService.getUserById(employeeId);

            if (employee == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
                return;
            }

            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/editEmployee.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving employee data");
        }
    }

    private void editEmployeeInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int employeeId;
        String username = request.getParameter("username");
        String contactInfo = request.getParameter("contactInfo");

        try {
            employeeId = Integer.parseInt(request.getParameter("employeeId"));

            if (employeeService.getUserById(employeeId) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
                return;
            }

            employeeService.updateEmployee(employeeId, username, contactInfo);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "Updated information about employee: " + employeeId);
            }

            response.sendRedirect("employee?action=viewAll");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating employee information");
        }
    }

    private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId;
        try {
            employeeId = Integer.parseInt(request.getParameter("employeeId"));
            User employee = employeeService.getUserById(employeeId);

            if (employee == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
                return;
            }

            request.setAttribute("employee", employee);
            request.getRequestDispatcher("/deleteEmployee.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving employee data");
        }
    }

    private void removeEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int employeeId;
        try {
            employeeId = Integer.parseInt(request.getParameter("employeeId"));

            if (employeeService.getUserById(employeeId) == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
                return;
            }

            employeeService.deleteEmployee(employeeId);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "Dismissed employee: " + employeeId);
            }

            response.sendRedirect("employee?action=viewAll");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid employee ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting employee");
        }
    }
}
