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
import services.ClientService;
import mapper.mapperImpl.UserMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @Mock
    private UserMapperImpl userMapper;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    public void testGetAllClients() throws Exception {
        mockMvc.perform(get("/client/allClient"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetClientById() throws Exception {
        UserDTO userDTO = new UserDTO(); // Populate with necessary fields
        when(clientService.getUserById(1)).thenReturn(userDTO);

        mockMvc.perform(get("/client/getIdClient/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddClient() throws Exception {
        UserDTO userDTO = new UserDTO(); // Populate with necessary fields
        when(userMapper.toUser(userDTO)).thenReturn(new User());

        mockMvc.perform(post("/client/addClient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateClient() throws Exception {
        mockMvc.perform(patch("/client/updateClient/1")
                .param("newName", "newName")
                .param("newContactInfo", "newContactInfo"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/client/deleteById/1"))
                .andExpect(status().isNoContent());
    }
}
