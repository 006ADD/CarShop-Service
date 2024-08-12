package repositories;

import model.AuditLog;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuditLogRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14.3")
            .withDatabaseName("carshopservice")
            .withUsername("userPostgres")
            .withPassword("passwordPostgres");

    private Connection connection;
    private AuditLogRepository auditLogRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        postgresContainer.start();

        connection = DriverManager.getConnection(
                postgresContainer.getJdbcUrl(),
                postgresContainer.getUsername(),
                postgresContainer.getPassword()
        );

        auditLogRepository = new AuditLogRepository(connection);

        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE audit_log (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INT, " +
                    "action VARCHAR(255), " +
                    "timestamp TIMESTAMP)");
        }
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (var stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE audit_log");
        }
        connection.close();
        postgresContainer.stop();
    }

    @Test
    public void testSaveAndFindAll() throws SQLException {
        User user = new User(1, "John Doe", "", "123-456-7890");
        AuditLog log = new AuditLog(1, user, "LOGIN", LocalDateTime.now());

        auditLogRepository.save(log);

        List<AuditLog> logs = auditLogRepository.findAll();
        assertNotNull(logs);
        assertFalse(logs.isEmpty());

        AuditLog retrievedLog = logs.get(0);
        assertEquals(log.getUser().getId(), retrievedLog.getUser().getId());
        assertEquals(log.getAction(), retrievedLog.getAction());
        assertEquals(log.getTimestamp(), retrievedLog.getTimestamp());
    }

    @Test
    public void testFindByUser() throws SQLException {
        User user = new User(1, "John Doe", "", "123-456-7890");
        AuditLog log1 = new AuditLog(1, user, "LOGIN", LocalDateTime.now().minusDays(1));
        AuditLog log2 = new AuditLog(2, user, "LOGOUT", LocalDateTime.now());

        auditLogRepository.save(log1);
        auditLogRepository.save(log2);

        List<AuditLog> logs = auditLogRepository.findByUser(user);
        assertNotNull(logs);
        assertEquals(2, logs.size());

        assertTrue(logs.stream().anyMatch(log -> "LOGIN".equals(log.getAction())));
        assertTrue(logs.stream().anyMatch(log -> "LOGOUT".equals(log.getAction())));
    }

    @Test
    public void testFindByAction() throws SQLException {
        User user = new User(1, "John Doe", "", "123-456-7890");
        AuditLog log1 = new AuditLog(1, user, "LOGIN", LocalDateTime.now());
        AuditLog log2 = new AuditLog(2, user, "LOGOUT", LocalDateTime.now());

        auditLogRepository.save(log1);
        auditLogRepository.save(log2);

        List<AuditLog> logs = auditLogRepository.findByAction("LOGIN");
        assertNotNull(logs);
        assertEquals(1, logs.size());
        assertEquals("LOGIN", logs.get(0).getAction());
    }
}
