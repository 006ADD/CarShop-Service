package controllers;

import dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import mapper.mapperImpl.OrderMapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapperImpl orderMapper;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllOrders(){
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

    @GetMapping("/getOneOrder/{id}")
    public ResponseEntity<?> getOrderId(@PathVariable int id){
        return ResponseEntity.ok().body(orderService.getOrderById(id));
    }

    @PostMapping("/createOrder/{order}")
    public ResponseEntity<?> createdOrder(@RequestBody OrderDTO orderDTO){
        orderService.createOrder(orderMapper.toOrder(orderDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/updateStatus/{id, status}")
    public ResponseEntity<?> updatedStatusOrder(@PathVariable  int id, @RequestParam String status){
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/getDeleteOrder/{id}")
    public ResponseEntity<?> getDeleteOrder(@PathVariable int id){
        orderService.cancelOrder(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
