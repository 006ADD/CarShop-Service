package servlet;

import model.User;
import services.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ClientServletTest {

    private ClientServlet clientServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private ClientService clientService;
    private UserService userService;
    private AuditService auditService;

    @BeforeEach
    public void setUp() {
        clientServlet = new ClientServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        clientService = mock(ClientService.class);
        userService = mock(UserService.class);
        auditService = mock(AuditService.class);


        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testViewAllClients() throws ServletException, IOException {
        Collection<User> users = new HashSet<>();
        users.add(new User(1, "user1", "client", "contact1", "password1"));
        users.add(new User(2, "user2", "client", "contact2", "password2"));

        when(clientService.getAllClient()).thenReturn(users);

        when(request.getParameter("action")).thenReturn("viewAll");

        clientServlet.doGet(request, response);

        verify(request).setAttribute("clients", users);
        verify(request.getRequestDispatcher("/viewAllClients.jsp")).forward(request, response);
    }

    @Test
    public void testAddNewClient() throws IOException {
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("contactInfo")).thenReturn("newcontact");

        User newUser = new User(1, "newuser", "client", "newcontact", "password");
        when(userService.getAllUsers()).thenReturn(Set.of(newUser));


        verify(response).sendRedirect("client?action=viewAll");
        verify(auditService).logAction(any(User.class), "A new client has been added: newuser");
    }

    @Test
    public void testEditClientInfo() throws IOException, ServletException {
        when(request.getParameter("clientId")).thenReturn("1");
        when(request.getParameter("username")).thenReturn("updateduser");
        when(request.getParameter("contactInfo")).thenReturn("updatedcontact");

        User existingUser = new User(1, "olduser", "client", "oldcontact", "password");
        when(clientService.getUserById(1)).thenReturn(existingUser);

        clientServlet.doPost(request, response);

        verify(clientService).updateClient(1, "updateduser", "updatedcontact");
        verify(response).sendRedirect("client?action=viewAll");
        verify(auditService).logAction(any(User.class), "Updated information about the client: 1");
    }

    @Test
    public void testRemoveClient() throws IOException {
        when(request.getParameter("clientId")).thenReturn("1");

        User existingUser = new User(1, "user", "client", "contact", "password");
        when(clientService.getUserById(1)).thenReturn(existingUser);


        verify(clientService).deleteClient(1);
        verify(response).sendRedirect("client?action=viewAll");
        verify(auditService).logAction(any(User.class), "Deleted client: 1");
    }

    @Test
    public void testShowEditClientForm() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("edit");

        clientServlet.doGet(request, response);

        verify(request.getRequestDispatcher("/editClientForm.jsp")).forward(request, response);
    }

    @Test
    public void testShowRemoveClientForm() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("remove");

        clientServlet.doGet(request, response);

        verify(request.getRequestDispatcher("/removeClientForm.jsp")).forward(request, response);
    }
    
    @Test
    public void testInvalidActionInDoGet() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("invalidAction");

        clientServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
    }
    
    @Test
    public void testInvalidActionInDoPost() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("invalidAction");

        clientServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
    }
}
