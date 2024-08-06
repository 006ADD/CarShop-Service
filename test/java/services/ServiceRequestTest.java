package services;

import model.SerRequest;
import services.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceRequestTest {

    private ServiceRequest serviceRequestService;

    @BeforeEach
    public void setUp() {
        serviceRequestService = new ServiceRequest();
    }

    @Test
    public void shouldCreateServiceRequest() {
        SerRequest request = new SerRequest(1, "Oil change");
        serviceRequestService.createServiceRequest(request);

        SerRequest retrievedRequest = serviceRequestService.getServiceRequestById(1);
        assertThat(retrievedRequest).isNotNull();
        assertThat(retrievedRequest.getId()).isEqualTo(1);
        assertThat(retrievedRequest.getDescription()).isEqualTo("Oil change");
        assertThat(retrievedRequest.getStatus()).isEqualTo("Pending"); // По умолчанию
    }

    @Test
    public void shouldUpdateServiceRequestStatus() {
        SerRequest request = new SerRequest(1, "Oil change");
        serviceRequestService.createServiceRequest(request);

        serviceRequestService.updateServiceRequestStatus(1, "Completed");

        SerRequest updatedRequest = serviceRequestService.getServiceRequestById(1);
        assertThat(updatedRequest).isNotNull();
        assertThat(updatedRequest.getStatus()).isEqualTo("Completed");
    }

    @Test
    public void shouldGetServiceRequestById() {
        SerRequest request = new SerRequest(1, "Oil change");
        serviceRequestService.createServiceRequest(request);

        SerRequest retrievedRequest = serviceRequestService.getServiceRequestById(1);
        assertThat(retrievedRequest).isNotNull();
        assertThat(retrievedRequest.getId()).isEqualTo(1);
    }

    @Test
    public void shouldGetAllServiceRequests() {
        SerRequest request1 = new SerRequest(1, "Oil change");
        SerRequest request2 = new SerRequest(2, "Brake check");
        serviceRequestService.createServiceRequest(request1);
        serviceRequestService.createServiceRequest(request2);

        Collection<SerRequest> requests = serviceRequestService.getAllServiceRequests();
        assertThat(requests).hasSize(2);
        assertThat(requests).extracting(SerRequest::getId).containsExactlyInAnyOrder(1, 2);
    }
}
