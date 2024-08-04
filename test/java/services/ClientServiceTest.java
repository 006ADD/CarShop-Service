package services;

import model.User;
import services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientServiceTest {

    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        clientService = new ClientService();
    }

    @Test
    public void shouldAddClient() {
        User client = new User(1, "John Doe", "john@example.com");
        clientService.addClient(client);

        User retrievedClient = clientService.getUserById(1);
        assertThat(retrievedClient).isNotNull();
        assertThat(retrievedClient.getName()).isEqualTo("John Doe");
        assertThat(retrievedClient.getContactInfo()).isEqualTo("john@example.com");
    }

    @Test
    public void shouldUpdateClient() {
        User client = new User(1, "John Doe", "john@example.com");
        clientService.addClient(client);

        clientService.updateClient(1, "Jane Doe", "jane@example.com");

        User updatedClient = clientService.getUserById(1);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getName()).isEqualTo("Jane Doe");
        assertThat(updatedClient.getContactInfo()).isEqualTo("jane@example.com");
    }

    @Test
    public void shouldDeleteClient() {
        User client = new User(1, "John Doe", "john@example.com");
        clientService.addClient(client);
        clientService.deleteClient(1);

        User deletedClient = clientService.getUserById(1);
        assertThat(deletedClient).isNull();
    }

    @Test
    public void shouldGetClientById() {
        User client = new User(1, "John Doe", "john@example.com");
        clientService.addClient(client);

        User retrievedClient = clientService.getUserById(1);
        assertThat(retrievedClient).isNotNull();
        assertThat(retrievedClient.getId()).isEqualTo(1);
        assertThat(retrievedClient.getName()).isEqualTo("John Doe");
        assertThat(retrievedClient.getContactInfo()).isEqualTo("john@example.com");
    }

}
