package za.co.example.core.impl;

import com.example.orders_service.models.OrderDTO;
import com.example.orders_service.models.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.example.core.services.IOrdersService;
import za.co.example.exceptions.OrderNotFoundException;
import za.co.example.exceptions.OrdersNotFoundException;
import za.co.example.mappers.OrderMapper;
import za.co.example.persistance.repositories.OrderRepository;
import za.co.example.proxy.ProductFeignClient;

import java.util.*;

@Slf4j
@Service
public class OrdersServiceImpl implements IOrdersService {

    private final OrderRepository orderRepository;
    private final ProductFeignClient productFeignClient;

    public OrdersServiceImpl(OrderRepository orderRepository, ProductFeignClient productFeignClient) {
        this.orderRepository = orderRepository;
        this.productFeignClient = productFeignClient;
    }

    @Override
    public void addOrder(OrderDTO orderDTO) {
        Map<String, Integer> productsToAdd = new HashMap<>();

        if (orderDTO.getProducts() != null) {
            for (Map.Entry<String, Integer> entry : orderDTO.getProducts().entrySet()) {
                String productName = entry.getKey();
                int requestedQuantity = entry.getValue() != null ? entry.getValue() : 1;

                ProductDTO productDTO = OrderMapper.ORDER_MAPPER
                        .productToProductDto(productFeignClient.getProduct(productName));

                if (productDTO != null) {
                    int availableQuantity = productDTO.getQuantity();
                    if (requestedQuantity <= availableQuantity) {
                        productsToAdd.put(productName, requestedQuantity);
                    } else {
                        throw new OrderNotFoundException("There are " + availableQuantity + " " + productName + " in stock!");
                    }
                } else {
                    throw new OrderNotFoundException("The are no products with name {}", productName);
                }
            }
        }

        String orderNumber = orderDTO.getOrderNumber();
        if (orderNumber == null) {
            Random random = new Random();
            int randomNumber = 10000 + random.nextInt(90000);
            orderDTO.setOrderNumber(String.valueOf(randomNumber));
        }

        orderDTO.setProducts(productsToAdd);

        orderRepository.save(OrderMapper.ORDER_MAPPER.orderDtoToOrder(orderDTO));
    }

        @Override
    public void removeOrder(UUID id) {
        boolean order = orderRepository.existsById(String.valueOf(id));

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
        OrderDTO orderOptional = OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findById(String.valueOf(id)).get());

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
    public OrderDTO getOrderByOrdererIdNo(String ordererIdNo) {
        return OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByOrdererIdNo(ordererIdNo));
    }

    @Override
    public List<OrderDTO> getOrderByProductName(String productName) {
        return OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findOrdersByProductName(productName));
    }

    @Override
    public void updateOrder(UUID id, OrderDTO updatedOrder) {
        OrderDTO existingOrder = getOrderById(id);
        if (existingOrder != null && updatedOrder != null) {
            if (updatedOrder.getOrderNumber() != null && !updatedOrder.getOrderNumber().isEmpty())
                existingOrder.setOrderNumber(updatedOrder.getOrderNumber());
            if (updatedOrder.getQuantity() != null) existingOrder.setQuantity(updatedOrder.getQuantity());
            if (updatedOrder.getProducts() != null && !updatedOrder.getProducts().isEmpty())
                existingOrder.setProducts(updatedOrder.getProducts());
            orderRepository.save(OrderMapper.ORDER_MAPPER.orderDtoToOrder(existingOrder));
        }

    }

    @Override
    public List<OrderDTO> searchOrders(UUID id, String orderNumber, Integer quantity, String product) {
        List<OrderDTO> orders = new ArrayList<>();
        if (id != null) {
            orders.add(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findById(String.valueOf(id)).get()));
        }
        if (orderNumber != null && !orderNumber.isEmpty()) {
            orders.addAll(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByOrderNumber(orderNumber)));
        }
        if (quantity != null) {
            orders.addAll(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByQuantity(quantity)));
        }

        return orders;
    }
}
