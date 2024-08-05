package services;

import model.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestServiceTest {

    private RequestService serviceRequestService;

    @BeforeEach
    public void setUp() {
        serviceRequestService = new RequestService();
    }

    @Test
    public void shouldCreateServiceRequest() {
        ServiceRequest request = new ServiceRequest(1, "Oil change");
        serviceRequestService.createServiceRequest(request);

        ServiceRequest retrievedRequest = serviceRequestService.getServiceRequestById(1);
        assertThat(retrievedRequest).isNotNull();
        assertThat(retrievedRequest.getId()).isEqualTo(1);
        assertThat(retrievedRequest.getDescription()).isEqualTo("Oil change");
        assertThat(retrievedRequest.getStatus()).isEqualTo("Pending"); // По умолчанию
    }

    @Test
    public void shouldUpdateServiceRequestStatus() {
        ServiceRequest request = new ServiceRequest(1, "Oil change");
        serviceRequestService.createServiceRequest(request);

        serviceRequestService.updateServiceRequestStatus(1, "Completed");

        ServiceRequest updatedRequest = serviceRequestService.getServiceRequestById(1);
        assertThat(updatedRequest).isNotNull();
        assertThat(updatedRequest.getStatus()).isEqualTo("Completed");
    }

    @Test
    public void shouldGetServiceRequestById() {
        ServiceRequest request = new ServiceRequest(1, "Oil change");
        serviceRequestService.createServiceRequest(request);

        ServiceRequest retrievedRequest = serviceRequestService.getServiceRequestById(1);
        assertThat(retrievedRequest).isNotNull();
        assertThat(retrievedRequest.getId()).isEqualTo(1);
    }

    @Test
    public void shouldGetAllServiceRequests() {
        ServiceRequest request1 = new ServiceRequest(1, "Oil change");
        ServiceRequest request2 = new ServiceRequest(2, "Brake check");
        serviceRequestService.createServiceRequest(request1);
        serviceRequestService.createServiceRequest(request2);

        Collection<ServiceRequest> requests = serviceRequestService.getAllServiceRequests();
        assertThat(requests).hasSize(2);
        assertThat(requests).extracting(ServiceRequest::getId).containsExactlyInAnyOrder(1, 2);
    }
}
