package servlet;

import model.SerRequest;
import services.ServiceRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@WebServlet("/service-request")
public class ServiceRequestServlet extends HttpServlet {
    private final ServiceRequest serviceRequest = new ServiceRequest(); // Убедитесь, что инициализируете этот сервис правильно

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "create":
                createServiceRequest(request, response);
                break;
            case "update":
                updateServiceRequestStatus(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "getById":
                getServiceRequestById(request, response);
                break;
            case "getAll":
                getAllServiceRequests(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    private void createServiceRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String description = request.getParameter("description");
            // Можно добавить другие параметры, если они есть
            SerRequest newRequest = new SerRequest(); // Заполните поля запроса по вашему требованию
            newRequest.setDescription(description);
            serviceRequest.createServiceRequest(newRequest);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().println("Service request created successfully.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating service request");
        }
    }

    private void updateServiceRequestStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");

            SerRequest requestToUpdate = serviceRequest.getServiceRequestById(id);
            if (requestToUpdate == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service request not found");
                return;
            }

            serviceRequest.updateServiceRequestStatus(id, status);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Service request status updated successfully.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating service request status");
        }
    }

    private void getServiceRequestById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            SerRequest requestFound = serviceRequest.getServiceRequestById(id);
            if (requestFound == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service request not found");
                return;
            }

            request.setAttribute("request", requestFound);
            request.getRequestDispatcher("/viewServiceRequest.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving service request");
        }
    }

    private void getAllServiceRequests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Collection<SerRequest> requests = serviceRequest.getAllServiceRequests();
            if (requests.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "No service requests found");
                return;
            }

            request.setAttribute("requests", requests);
            request.getRequestDispatcher("/viewAllServiceRequests.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving service requests");
        }
    }
}
