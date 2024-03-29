package za.co.example.core.services;

import com.example.orders_service.models.OrderDTO;

import java.util.List;
import java.util.UUID;

public interface IOrdersService {

    void addOrder(OrderDTO orderDTO);

    void removeOrder(UUID id);

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(UUID id);

    List<OrderDTO> getOrdersByNumber(String orderNumber);

    List<OrderDTO> getOrdersByQuality(Integer quality);

    OrderDTO getOrderByOrdererIdNo(String ordererIdNo);

    void updateOrder(UUID id, OrderDTO updatedOrder);

    List<OrderDTO> searchOrders(UUID id, String orderNumber, Integer quality);
}
