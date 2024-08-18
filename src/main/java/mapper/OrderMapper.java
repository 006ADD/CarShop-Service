package mapper;

import dto.OrderDTO;
import model.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    OrderDTO toOrderDTO(Order order);

    Order toOrder(OrderDTO orderDTO);
}
