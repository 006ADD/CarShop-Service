package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class CarShopServletTest {

    private CarShopServlet carShopServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        carShopServlet = new CarShopServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testLoginAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("login");

        carShopServlet.doGet(request, response);

        verify(request).getRequestDispatcher("/login");
        verify(request.getRequestDispatcher("/login")).forward(request, response);
    }

    @Test
    public void testMainAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("main");

        carShopServlet.doGet(request, response);

        verify(request).getRequestDispatcher("/main");
        verify(request.getRequestDispatcher("/main")).forward(request, response);
    }

    @Test
    public void testInvalidAction() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("invalidAction");

        carShopServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
