package servlet;

import model.Car;
import services.CarService;
import services.AuditService;
import model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class CarServletTest {

    private CarServlet carServlet;
    private CarService carService;
    private AuditService auditService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() {
        carService = mock(CarService.class);
        auditService = mock(AuditService.class);
        carServlet = new CarServlet();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        writer = mock(PrintWriter.class);

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testAddCarSuccess() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("brand")).thenReturn("Toyota");
        when(request.getParameter("model")).thenReturn("Corolla");
        when(request.getParameter("year")).thenReturn("2020");
        when(request.getParameter("price")).thenReturn("20000");
        when(request.getParameter("condition")).thenReturn("new");
        when(request.getParameter("status")).thenReturn("available");

        User currentUser = new User();
        when(session.getAttribute("user")).thenReturn(currentUser);

        carServlet.doPost(request, response);

        verify(carService).addCar(any(Car.class));
        verify(auditService).logAction(eq(currentUser), contains("A car has been added"));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(writer).println("The car has been added successfully.");
    }

    @Test
    public void testEditCarSuccess() throws Exception {
        when(request.getParameter("action")).thenReturn("edit");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("brand")).thenReturn("Honda");
        when(request.getParameter("model")).thenReturn("Civic");
        when(request.getParameter("year")).thenReturn("2021");
        when(request.getParameter("price")).thenReturn("22000");
        when(request.getParameter("condition")).thenReturn("new");
        when(request.getParameter("status")).thenReturn("available");

        Car existingCar = new Car(1, "Toyota", "Corolla", 2020, 20000, "new", "available");
        when(carService.getCar(1)).thenReturn(existingCar);

        User currentUser = new User();
        when(session.getAttribute("user")).thenReturn(currentUser);

        carServlet.doPost(request, response);

        verify(carService).updateCar(eq(1), any(Car.class));
        verify(auditService).logAction(eq(currentUser), contains("The car ID has been edited"));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).println("The car has been successfully updated.");
    }

    @Test
    public void testDeleteCarSuccess() throws Exception {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("1");

        Car existingCar = new Car(1, "Toyota", "Corolla", 2020, 20000, "new", "available");
        when(carService.getCar(1)).thenReturn(existingCar);

        User currentUser = new User();
        when(session.getAttribute("user")).thenReturn(currentUser);

        carServlet.doPost(request, response);

        verify(carService).deleteCar(eq(1));
        verify(auditService).logAction(eq(currentUser), contains("The car ID was deleted"));
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).println("The car was successfully deleted.");
    }

    @Test
    public void testViewCarsSuccess() throws Exception {
        when(request.getParameter("action")).thenReturn("view");
        when(carService.getAllCars()).thenReturn(Collections.singletonList(new Car(1, "Toyota", "Corolla", 2020, 20000, "new", "available")));

        carServlet.doGet(request, response);

        verify(request).setAttribute(eq("cars"), any(Collection.class));
        verify(request.getRequestDispatcher("/viewCars.jsp")).forward(request, response);
    }

    @Test
    public void testAddCarFailureDueToInvalidParameter() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("brand")).thenReturn("Toyota");
        when(request.getParameter("model")).thenReturn(null); // Invalid parameter

        carServlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid parameters");
    }
}
