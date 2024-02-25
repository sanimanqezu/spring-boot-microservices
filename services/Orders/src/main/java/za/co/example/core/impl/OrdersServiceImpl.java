package za.co.example.core.impl;

import com.example.orders_service.models.OrderDTO;
import org.springframework.stereotype.Service;
import za.co.example.core.services.IOrdersService;
import za.co.example.exceptions.OrderNotFoundException;
import za.co.example.exceptions.OrdersNotFoundException;
import za.co.example.mappers.OrderMapper;
import za.co.example.persistance.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersServiceImpl implements IOrdersService {

    private final OrderRepository orderRepository;

    public OrdersServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void addOrder(OrderDTO orderDTO) {
        String orderNumber = orderDTO.getOrderNumber();

        if (orderNumber == null) {
            throw new OrderNotFoundException("No order number found");
        }
        orderRepository.save(OrderMapper.ORDER_MAPPER.orderDtoToOrder(orderDTO));

    }

    @Override
    public void removeOrder(UUID id) {
        boolean order = orderRepository.existsById(id);

        if (!order) {
            throw new OrderNotFoundException("Id", id);
        }
        orderRepository.delete(OrderMapper.ORDER_MAPPER.orderDtoToOrder(getOrderById(id)));

    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orders = OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findAll());

        if (orders.isEmpty()) {
            throw new OrdersNotFoundException("No order was found!!");
        }
        return orders;
    }

    @Override
    public OrderDTO getOrderById(UUID id) {
        OrderDTO orderOptional = OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findById(id).get());

        if (orderOptional == null) {
            throw new OrderNotFoundException("Id", id);
        }
        return orderOptional;
    }

    @Override
    public List<OrderDTO> getOrdersByNumber(String orderNumber) {
        List<OrderDTO> orders = OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByOrderNumber(orderNumber));
        if (orders == null || orders.isEmpty()) {
            throw new OrdersNotFoundException("Order Number", orderNumber);
        }
        return orders;
    }

    @Override
    public List<OrderDTO> getOrdersByQuality(Integer quantity) {
        List<OrderDTO> orders = OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByQuantity(quantity));
        if (orders == null || orders.isEmpty()) {
            throw new OrdersNotFoundException("Quality", quantity);
        }
        return orders;
    }

    @Override
    public List<OrderDTO> getOrdersByProduct(String product) {
        List<OrderDTO> orders = OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByProduct(product));
        if (orders == null || orders.isEmpty()) {
            throw new OrdersNotFoundException("Product", product);
        }
        return orders;
    }

    @Override
    public void updateOrder(UUID id, OrderDTO updatedOrder) {
        OrderDTO existingOrder = getOrderById(id);
        if (existingOrder != null && updatedOrder != null) {
            if (updatedOrder.getOrderNumber() != null && !updatedOrder.getOrderNumber().isEmpty()) existingOrder.setOrderNumber(updatedOrder.getOrderNumber());
            if (updatedOrder.getQuantity() != null ) existingOrder.setQuantity(updatedOrder.getQuantity());
            if (updatedOrder.getProducts() != null && !updatedOrder.getProducts().isEmpty()) existingOrder.setProducts(updatedOrder.getProducts());
            orderRepository.save(OrderMapper.ORDER_MAPPER.orderDtoToOrder(existingOrder));
        }

    }

    @Override
    public List<OrderDTO> searchOrders(UUID id, String orderNumber, Integer quantity, String product) {
        List<OrderDTO> orders = new ArrayList<>();
        if (id != null) {
            orders.add(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findById(id).get()));
        }
        if (orderNumber != null && !orderNumber.isEmpty()) {
            orders.addAll(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByOrderNumber(orderNumber)));
        }
        if (quantity != null) {
            orders.addAll(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByQuantity(quantity)));
        }
        if (product != null && !product.isEmpty()) {
            orders.addAll(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByProduct(product)));
        }

        return orders;
    }
}
