package mapper;

import dto.OrderDTO;
import javax.annotation.processing.Generated;
import model.Order;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-19T01:22:39+0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDTO toOrderDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        return orderDTO;
    }

    @Override
    public Order toOrder(OrderDTO orderDTO) {
        if ( orderDTO == null ) {
            return null;
        }

        int id = 0;
        String status = null;

        Order order = new Order( id, status );

        return order;
    }
}
