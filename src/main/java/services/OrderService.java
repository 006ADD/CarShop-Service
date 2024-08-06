package services;

import model.Order;
import model.SerRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class OrderService {
    private Map<Integer, Order> orders = new HashMap<>();
    private Map<Integer, SerRequest> serviceRequests = new HashMap<>();

    public void createOrder(Order order){
        orders.put(order.getId(), order);
    }

    public void updateOrderStatus(int id, String status){
        Order order = orders.get(id);
        if(order != null){
            order.setStatus(status);
        }
    }

    public Collection<Order> getAllOrders(){
        return orders.values();
    }

    public void cancelOrder(int id){
        orders.remove(id);
    }


    public Order getOrderById(int orderId) {
        return orders.get(orderId);
    }
}
