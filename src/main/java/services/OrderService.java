package services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Order;
import model.SerRequest;
import repositories.OrderRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class OrderService {
    private  OrderRepository orderRepository;

    public void createOrder(Order order){
        orderRepository.createOrder(order);
    }

    public void updateOrderStatus(int id, String status){
        orderRepository.updateOrderStatus(id, status);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public void cancelOrder(int id){
        orderRepository.remove(id);
    }


    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId);
    }
}
