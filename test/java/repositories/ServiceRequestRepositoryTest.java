package repositories;

import model.SerRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import utils.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceRequestRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14.3")
            .withDatabaseName("carshopservice")
            .withUsername("userPostgres")
            .withPassword("passwordPostgres");

    private Connection connection;
    private ServiceRequestRepository serviceRequestRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        postgresContainer.start();

        connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );

        serviceRequestRepository = new ServiceRequestRepository(new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                return connection;
            }
        });

        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE service_requests (" +
                    "id SERIAL PRIMARY KEY, " +
                    "description VARCHAR(255), " +
                    "status VARCHAR(50))");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (var stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE service_requests");
        }
        connection.close();
        postgresContainer.stop();
    }

    @Test
    public void testCreateAndFindById() {
        SerRequest request = new SerRequest(1, "Oil change", "PENDING");

        serviceRequestRepository.create(request);

        SerRequest retrievedRequest = serviceRequestRepository.findById(1);
        assertNotNull(retrievedRequest);
        assertEquals(request.getId(), retrievedRequest.getId());
        assertEquals(request.getDescription(), retrievedRequest.getDescription());
        assertEquals(request.getStatus(), retrievedRequest.getStatus());
    }

    @Test
    public void testUpdate() {
        SerRequest request = new SerRequest(1, "Oil change", "PENDING");
        serviceRequestRepository.create(request);

        serviceRequestRepository.update(1, "COMPLETED");

        SerRequest updatedRequest = serviceRequestRepository.findById(1);
        assertNotNull(updatedRequest);
        assertEquals("COMPLETED", updatedRequest.getStatus());
    }

    @Test
    public void testFindAll() {
        SerRequest request1 = new SerRequest(1, "Oil change", "PENDING");
        SerRequest request2 = new SerRequest(2, "Brake repair", "IN_PROGRESS");
        serviceRequestRepository.create(request1);
        serviceRequestRepository.create(request2);

        Collection<SerRequest> requests = serviceRequestRepository.findAll();
        assertNotNull(requests);
        assertEquals(2, requests.size());

        assertTrue(requests.stream().anyMatch(r -> "Oil change".equals(r.getDescription())));
        assertTrue(requests.stream().anyMatch(r -> "Brake repair".equals(r.getDescription())));
    }
}
