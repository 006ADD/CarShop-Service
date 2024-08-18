package services;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void shouldRegisterUser() {
        // Arrange
        User user = new User(1, "username", "client", "contact", "password");

        // Act
        userService.registerUser(user);

        // Assert
        assertThat(userService.getAllUsers()).contains(user);
    }

    @Test
    void shouldLoginUserSuccessfully() {
        // Arrange
        User user = new User(1, "username", "client", "contact", "password");
        userService.registerUser(user);

        // Act
        User loggedInUser = userService.loginUser("username", "password");

        // Assert
        assertThat(loggedInUser).isEqualTo(user);
    }

    @Test
    void shouldNotLoginUserWithWrongPassword() {
        // Arrange
        User user = new User(1, "username", "client", "contact", "password");
        userService.registerUser(user);

        // Act
        User loggedInUser = userService.loginUser("username", "wrongpassword");

        // Assert
        assertThat(loggedInUser).isNull();
    }
}

