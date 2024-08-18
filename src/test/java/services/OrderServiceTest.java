package services;

import model.Order;
import services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceTest {

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService();
    }

    @Test
    public void shouldCreateOrder() {
        Order order = new Order(1, "Pending");
        orderService.createOrder(order);

        Order retrievedOrder = orderService.getOrderById(1);
        assertThat(retrievedOrder).isNotNull();
        assertThat(retrievedOrder.getId()).isEqualTo(1);
        assertThat(retrievedOrder.getStatus()).isEqualTo("Pending");
    }

    @Test
    public void shouldUpdateOrderStatus() {
        Order order = new Order(1, "Pending");
        orderService.createOrder(order);

        orderService.updateOrderStatus(1, "Completed");

        Order updatedOrder = orderService.getOrderById(1);
        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getStatus()).isEqualTo("Completed");
    }

    @Test
    public void shouldCancelOrder() {
        Order order = new Order(1, "Pending");
        orderService.createOrder(order);
        orderService.cancelOrder(1);

        Order cancelledOrder = orderService.getOrderById(1);
        assertThat(cancelledOrder).isNull();
    }

    @Test
    public void shouldGetAllOrders() {
        Order order1 = new Order(1, "Pending");
        Order order2 = new Order(2, "Completed");
        orderService.createOrder(order1);
        orderService.createOrder(order2);

        Collection<Order> orders = orderService.getAllOrders();
        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getId).containsExactlyInAnyOrder(1, 2);
    }

    @Test
    public void shouldGetOrderById() {
        Order order = new Order(1, "Pending");
        orderService.createOrder(order);

        Order retrievedOrder = orderService.getOrderById(1);
        assertThat(retrievedOrder).isNotNull();
        assertThat(retrievedOrder.getId()).isEqualTo(1);
    }
}
