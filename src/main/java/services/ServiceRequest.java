package services;

import model.SerRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServiceRequest {
    private final Map<Integer, SerRequest> serviceRequests = new HashMap<>();

    public void createServiceRequest(SerRequest request) {
        serviceRequests.put(request.getId(), request);
    }

    public void updateServiceRequestStatus(int id, String status) {
        SerRequest serRequest = serviceRequests.get(id);
        if (serRequest != null) {
            serRequest.setStatus(status);
        }
    }

    public SerRequest getServiceRequestById(int id) {
        return serviceRequests.get(id);
    }

    public Collection<SerRequest> getAllServiceRequests() {
        return serviceRequests.values();
    }
}
