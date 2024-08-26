package controller;

import dto.OrderDTO;
import model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.OrderService;
import mapper.mapperImpl.OrderMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderMapperImpl orderMapper;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/order/getAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetOrderById() throws Exception {
        OrderDTO orderDTO = new OrderDTO(); // Populate with necessary fields
        when(orderService.getOrderById(1)).thenReturn(orderDTO);

        mockMvc.perform(get("/order/getOneOrder/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateOrder() throws Exception {
        OrderDTO orderDTO = new OrderDTO(); // Populate with necessary fields
        when(orderMapper.toOrder(orderDTO)).thenReturn(new Order());

        mockMvc.perform(post("/order/createOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateStatusOrder() throws Exception {
        mockMvc.perform(patch("/order/updateStatus/1")
                .param("status", "Shipped"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/order/deleteOrder/1"))
                .andExpect(status().isNoContent());
    }
}
