package mapper.mapperImpl;

import dto.OrderDTO;
import mapper.CarMapper;
import mapper.OrderMapper;
import mapper.UserMapper;
import model.Order;

public class OrderMapperImpl implements OrderMapper {
    private final UserMapper userMapper = new UserMapperImpl();
    private final CarMapper carMapper = new CarMapperImpl();
    @Override
    public OrderDTO toOrderDTO(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderDTO(
                order.getId(),
                userMapper.toUserDTO(order.getClient()),
                carMapper.toCarDTO(order.getCar()),
                order.getOrderDate(),
                order.getStatus()
        );
    }

    @Override
    public Order toOrder(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        return new Order(
                orderDTO.getId(),
                userMapper.toUser(orderDTO.getClient()),
                carMapper.toCar(orderDTO.getCar()),
                orderDTO.getOrderDate(),
                orderDTO.getStatus()
        );
    }
}
