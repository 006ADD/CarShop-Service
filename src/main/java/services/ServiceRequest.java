package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.SerRequest;
import repositories.ServiceRequestRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
    private ServiceRequestRepository serviceRequestRepository;


    public void createServiceRequest(SerRequest request) {
        serviceRequestRepository.create(request);    }

    public void updateServiceRequestStatus(int id, String status) {
        serviceRequestRepository.update(id, status);
    }

    public SerRequest getServiceRequestById(int id) {
        return serviceRequestRepository.findById(id);
    }

    public Collection<SerRequest> getAllServiceRequests() {
        return serviceRequestRepository.findAll();
    }
}
