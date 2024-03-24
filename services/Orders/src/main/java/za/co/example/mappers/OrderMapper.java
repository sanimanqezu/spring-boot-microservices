package za.co.example.mappers;

import com.example.orders_service.models.OrderDTO;
import com.example.orders_service.models.OrderItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import za.co.example.persistance.entities.Order;
import za.co.example.persistance.entities.OrderItem;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class) ;

    OrderDTO orderToOrderDto(Order order);

    Order orderDtoToOrder(OrderDTO orderDTO);

    List<OrderDTO> orderToOrderDto(List<Order> order);

    OrderItemDTO orderItemToOrderItemDto(OrderItem orderItem);

    OrderItem orderItemDtoToOrderItem(OrderItemDTO orderItemDTO);

}
