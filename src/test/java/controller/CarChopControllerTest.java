package controller;

import dto.UserDTO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import services.UserService;
import mapper.mapperImpl.UserMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CarChopControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private CarChopController carChopController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carChopController).build();
    }

    @Test
    public void testLogin() throws Exception {
        String name = "testuser";
        String password = "password123";
        User user = new User(); // Populate with necessary fields
        UserDTO userDTO = new UserDTO(); // Populate with necessary fields
        
        // Mocking userService.loginUser
        when(userService.loginUser(name, password)).thenReturn(user);

        mockMvc.perform(get("/carshop/login")
                .param("name", name)
                .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name)); // Adjust based on actual fields
    }

    @Test
    public void testRegister() throws Exception {
        UserDTO userDTO = new UserDTO(); // Populate with necessary fields
        User user = new User(); // Populate with necessary fields
        
        // Mocking userMapper.toUser and userService.registerUser
        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(userService.registerUser(user)).thenReturn(user);

        mockMvc.perform(post("/carshop/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(userDTO.getName())); // Adjust based on actual fields
    }
}
