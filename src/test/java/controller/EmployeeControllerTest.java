package controller;

import dto.UserDTO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.EmployeeService;
import mapper.mapperImpl.UserMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void testAddEmployee() throws Exception {
        UserDTO userDTO = new UserDTO(); // Populate with necessary fields
        when(userMapper.toUser(userDTO)).thenReturn(new User());

        mockMvc.perform(post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/employee/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        User user = new User(); // Populate with necessary fields
        when(employeeService.getAllEmployees()).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists()); // Adjust based on actual fields
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User(); // Populate with necessary fields
        when(employeeService.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        mockMvc.perform(put("/employee/1")
                .param("newName", "New Name")
                .param("newContactInfo", "New Contact Info"))
                .andExpect(status().isNoContent());
    }
}
