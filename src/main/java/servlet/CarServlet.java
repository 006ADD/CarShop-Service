package servlet;

import model.Car;
import services.*;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@WebServlet("/car")
public class CarServlet extends HttpServlet {
    private final CarService carService = new CarService();
    private final AuditService auditService = new AuditService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addCar(request, response);
                break;
            case "edit":
                editCar(request, response);
                break;
            case "delete":
                deleteCar(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("view".equals(action)) {
            viewCars(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    private void addCar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            int year = Integer.parseInt(request.getParameter("year"));
            double price = Double.parseDouble(request.getParameter("price"));
            String condition = request.getParameter("condition");
            String status = request.getParameter("status");

            if (brand == null || model == null || year <= 0 || price <= 0 || condition == null || status == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid parameters");
                return;
            }

            Car car = new Car(carService.getAllCars().size() + 1, brand, model, year, price, condition, status);
            carService.addCar(car);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "A car has been added: " + brand + " " + model);
            }

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("The car has been added successfully.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding car");
        }
    }

    private void editCar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            int year = Integer.parseInt(request.getParameter("year"));
            double price = Double.parseDouble(request.getParameter("price"));
            String condition = request.getParameter("condition");
            String status = request.getParameter("status");

            Car car = carService.getCar(id);
            if (car == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
                return;
            }

            Car updatedCar = new Car(id, brand, model, year, price, condition, status);
            carService.updateCar(id, updatedCar);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "The car ID has been edited: " + id);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("The car has been successfully updated.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating car");
        }
    }

    private void deleteCar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            Car car = carService.getCar(id);
            if (car == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
                return;
            }

            carService.deleteCar(id);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "The car ID was deleted: " + id);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("The car was successfully deleted.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting car");
        }
    }

    private void viewCars(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Collection<Car> cars = carService.getAllCars();
            if (cars.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No cars found");
                return;
            }

            request.setAttribute("cars", cars);
            request.getRequestDispatcher("/viewCars.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving cars");
        }
    }
}
