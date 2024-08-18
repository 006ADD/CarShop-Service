package repositories;

import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import utils.DataSource;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private static PostgreSQLContainer<?> postgresContainer;
    private static UserRepository userRepository;

    @BeforeAll
    public static void setUp() {
        postgresContainer = new PostgreSQLContainer<>("postgres:latest")
                .withDatabaseName("carshopservice")
                .withUsername("userPostgres")
                .withPassword("passwordPostgres");

        postgresContainer.start();
        DataSource.setJdbcUrl(postgresContainer.getJdbcUrl());
        DataSource.setUsername(postgresContainer.getUsername());
        DataSource.setPassword(postgresContainer.getPassword());

        userRepository = new UserRepository();
    }

    @AfterAll
    public static void tearDown() {
        postgresContainer.stop();
    }

    @Test
    public void testRegisterAndFindById() {
        User user = new User("JohnDoe", "password", "admin");
        userRepository.register(user);

        User fetchedUser = userRepository.findById(user.getId());
        assertNotNull(fetchedUser);
        assertEquals(user.getName(), fetchedUser.getName());
    }

    @Test
    public void testLogin() {
        User user = new User("JaneDoe", "password", "user");
        userRepository.register(user);

        User loggedInUser = userRepository.login("JaneDoe", "password");
        assertNotNull(loggedInUser);
        assertEquals("JaneDoe", loggedInUser.getName());
    }

    @Test
    public void testFindAll() {
        User user1 = new User("User1", "password1", "role1");
        User user2 = new User("User2", "password2", "role2");
        userRepository.register(user1);
        userRepository.register(user2);

        Collection<User> users = userRepository.findAll();
        assertTrue(users.size() >= 2);
    }

    @Test
    public void testRemove() {
        User user = new User("UserToRemove", "password", "role");
        userRepository.register(user);
        userRepository.remove(user.getId());

        User fetchedUser = userRepository.findById(user.getId());
        assertNull(fetchedUser);
    }
}
