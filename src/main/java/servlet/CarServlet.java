package servlet;

import mapper.CarMapper;
import mapper.mapperImpl.CarMapperImpl;
import model.Car;
import dto.CarDTO;
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
    private final CarMapper carMapper = new CarMapperImpl(); // Use the mapper implementation

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
            CarDTO carDTO = new CarDTO(
                    0, // ID will be auto-generated
                    request.getParameter("brand"),
                    request.getParameter("model"),
                    Integer.parseInt(request.getParameter("year")),
                    Double.parseDouble(request.getParameter("price")),
                    request.getParameter("condition"),
                    request.getParameter("status")
            );

            Car car = carMapper.toCar(carDTO);

            if (car.getBrand() == null || car.getModel() == null || car.getYear() <= 0 || car.getPrice() <= 0 || car.getCondition() == null || car.getStatus() == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid parameters");
                return;
            }

            car.setId(carService.getAllCars().size() + 1); // Set ID
            carService.addCar(car);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "A car has been added: " + car.getBrand() + " " + car.getModel());
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
            CarDTO carDTO = new CarDTO(
                    Integer.parseInt(request.getParameter("id")),
                    request.getParameter("brand"),
                    request.getParameter("model"),
                    Integer.parseInt(request.getParameter("year")),
                    Double.parseDouble(request.getParameter("price")),
                    request.getParameter("condition"),
                    request.getParameter("status")
            );

            Car updatedCar = carMapper.toCar(carDTO);
            Car existingCar = carService.getCar(updatedCar.getId());
            if (existingCar == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Car not found");
                return;
            }

            carService.updateCar(updatedCar.getId(), updatedCar);

            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");
            if (currentUser != null) {
                auditService.logAction(currentUser, "The car ID has been edited: " + updatedCar.getId());
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
