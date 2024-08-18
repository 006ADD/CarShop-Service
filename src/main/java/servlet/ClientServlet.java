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
import java.util.Collection;
import java.util.List;

@WebServlet("/client")
public class ClientServlet extends HttpServlet {
    private final ClientService clientService = new ClientService();
    private final UserService userService = new UserService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "viewAll":
                viewAllClients(request, response);
                break;
            case "edit":
                showEditClientForm(request, response);
                break;
            case "remove":
                showRemoveClientForm(request, response);
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
                addNewClient(request, response);
                break;
            case "edit":
                editClientInfo(request, response);
                break;
            case "remove":
                removeClient(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    private void viewAllClients(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Collection<User> clients = clientService.getAllClient();
            if (clients.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No clients found");
                return;
            }
            request.setAttribute("clients", clients);
            request.getRequestDispatcher("/viewAllClients.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving clients");
        }
    }

    private void addNewClient(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String contactInfo = request.getParameter("contactInfo");

        if (username == null || password == null || contactInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            User client = new User(userService.getAllUsers().size() + 1, username, "client", contactInfo, password);
            userService.registerUser(client);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "A new client has been added: " + username);
            }

            response.sendRedirect("client?action=viewAll");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding client");
        }
    }

    private void showEditClientForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/editClientForm.jsp").forward(request, response);
    }

    private void editClientInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientId = 0;
        try {
            clientId = Integer.parseInt(request.getParameter("clientId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID format");
            return;
        }

        String username = request.getParameter("username");
        String contactInfo = request.getParameter("contactInfo");

        if (clientId <= 0 || username == null || contactInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
            return;
        }

        try {
            User client = clientService.getUserById(clientId);
            if (client == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Client not found");
                return;
            }

            clientService.updateClient(clientId, username, contactInfo);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "Updated information about the client: " + clientId);
            }

            response.sendRedirect("client?action=viewAll");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating client information");
        }
    }

    private void showRemoveClientForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/removeClientForm.jsp").forward(request, response);
    }

    private void removeClient(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientId = 0;
        try {
            clientId = Integer.parseInt(request.getParameter("clientId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID format");
            return;
        }

        if (clientId <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID");
            return;
        }

        try {
            User client = clientService.getUserById(clientId);
            if (client == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Client not found");
                return;
            }

            clientService.deleteClient(clientId);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "Deleted client: " + clientId);
            }

            response.sendRedirect("client?action=viewAll");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error removing client");
        }
    }
}
