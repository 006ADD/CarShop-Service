package repositories;

import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import utils.DataSource;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ClientRepositoryTest {

    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14.3")
            .withDatabaseName("carshopservice")
            .withUsername("userPostgres")
            .withPassword("passwordPostgres");

    private DataSource dataSource;
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() throws Exception {
        // Start the PostgreSQL container
        postgresContainer.start();

        dataSource = new DataSource();
        dataSource.setJdbcUrl(postgresContainer.getJdbcUrl());
        dataSource.setUsername(postgresContainer.getUsername());
        dataSource.setPassword(postgresContainer.getPassword());

        clientRepository = new ClientRepository(dataSource);

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE clients (id SERIAL PRIMARY KEY, name VARCHAR(255), contact_info VARCHAR(255))");
        }
    }

    @AfterEach
    public void tearDown() {
        postgresContainer.stop();
    }

    @Test
    public void testAddAndGetClient() {
        User client = new User(1, "John Doe", "", "123-456-7890");
        clientRepository.add(client);

        User retrievedClient = clientRepository.getUserById(1);
        assertNotNull(retrievedClient);
        assertEquals("John Doe", retrievedClient.getName());
        assertEquals("123-456-7890", retrievedClient.getContactInfo());
    }

    @Test
    public void testUpdateClient() {
        User client = new User(1, "John Doe", "", "123-456-7890");
        clientRepository.add(client);

        clientRepository.update(1, "Jane Doe", "987-654-3210");
        User updatedClient = clientRepository.getUserById(1);
        assertNotNull(updatedClient);
        assertEquals("Jane Doe", updatedClient.getName());
        assertEquals("987-654-3210", updatedClient.getContactInfo());
    }

    @Test
    public void testDeleteClient() {
        User client = new User(1, "John Doe", "", "123-456-7890");
        clientRepository.add(client);

        clientRepository.delete(1);
        User deletedClient = clientRepository.getUserById(1);
        assertNull(deletedClient);
    }

    @Test
    public void testFindAllClients() {
        User client1 = new User(1, "John Doe", "", "123-456-7890");
        User client2 = new User(2, "Jane Doe", "", "987-654-3210");
        clientRepository.add(client1);
        clientRepository.add(client2);

        Collection<User> clients = clientRepository.findAll();
        assertEquals(2, clients.size());
    }
}
