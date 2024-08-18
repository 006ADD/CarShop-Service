package services;

import model.AuditLog;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

public class AuditServiceTest {

    private AuditService auditService;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        auditService = new AuditService();
        user1 = new User(1, "Ivan", "client", "ivan@example.com", "password");
        user2 = new User(2, "Anna", "client", "anna@example.com", "password");
    }

    @Test
    void logAction_ShouldAddLogToAuditLogs() {
        auditService.logAction(user1, "Login");

        List<AuditLog> logs = auditService.getLogs();
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getUser()).isEqualTo(user1);
        assertThat(logs.get(0).getAction()).isEqualTo("Login");
        assertThat(logs.get(0).getId()).isEqualTo(1);
        assertThat(logs.get(0).getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void getLogs_ShouldReturnAllAuditLogs() {
        auditService.logAction(user1, "Login");
        auditService.logAction(user2, "Logout");

        List<AuditLog> logs = auditService.getLogs();

        assertThat(logs).hasSize(2);
        assertThat(logs).extracting("user").containsExactly(user1, user2);
        assertThat(logs).extracting("action").containsExactly("Login", "Logout");
    }

    @Test
    void filterLogsByUser_ShouldReturnLogsForSpecificUser() {
        auditService.logAction(user1, "Login");
        auditService.logAction(user2, "Logout");
        auditService.logAction(user1, "UpdateProfile");

        List<AuditLog> logs = auditService.filterLogsByUser(user1);

        assertThat(logs).hasSize(2);
        assertThat(logs).extracting("user").containsOnly(user1);
        assertThat(logs).extracting("action").containsExactly("Login", "UpdateProfile");
    }

    @Test
    void filterLogsByAction_ShouldReturnLogsForSpecificAction() {
        auditService.logAction(user1, "Login");
        auditService.logAction(user2, "Login");
        auditService.logAction(user1, "Logout");

        List<AuditLog> logs = auditService.filterLogsByAction("Login");

        assertThat(logs).hasSize(2);
        assertThat(logs).extracting("user").containsExactly(user1, user2);
        assertThat(logs).extracting("action").containsOnly("Login");
    }
}
