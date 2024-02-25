package za.co.example.mappers;

import com.example.orders_service.models.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import za.co.example.persistance.entities.Order;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class) ;

    OrderDTO orderToOrderDto(Order order);
    Order orderDtoToOrder(OrderDTO orderDTO);

    List<OrderDTO> orderToOrderDto(List<Order> order);

}
