package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.CarController;
import dto.CarDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import services.CarService;
import mapper.CarMapper;

public class CarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarService carService;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    public void testAddCar() throws Exception {
        CarDTO carDTO = new CarDTO(); // Populate with required fields

        mockMvc.perform(post("/car/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(carDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetCarById() throws Exception {
        CarDTO carDTO = new CarDTO(); // Populate with required fields

        mockMvc.perform(get("/car/getCarOne/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1)); // Update with actual field
    }

    @Test
    public void testGetAllCars() throws Exception {
        mockMvc.perform(get("/car/getAllCars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testEditCar() throws Exception {
        CarDTO carDTO = new CarDTO(); // Populate with required fields

        mockMvc.perform(patch("/car/editCar/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(carDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCar() throws Exception {
        mockMvc.perform(delete("/car/deleteCarId/1"))
                .andExpect(status().isOk());
    }
}
