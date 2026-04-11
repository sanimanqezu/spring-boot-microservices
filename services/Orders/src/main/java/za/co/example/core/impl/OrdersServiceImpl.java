package za.co.example.core.impl;

import com.example.orders_service.models.OrderDTO;
import com.example.orders_service.models.OrderItemDTO;
import com.example.orders_service.models.ProductDTO;
import com.example.orders_service.models.UserDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.example.core.services.IOrdersService;
import za.co.example.exceptions.OrderNotFoundException;
import za.co.example.exceptions.OrdersNotFoundException;
import za.co.example.mappers.OrderMapper;
import za.co.example.persistance.repositories.OrderRepository;
import za.co.example.proxy.ProductFeignClient;
import za.co.example.proxy.UserFeignClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class OrdersServiceImpl implements IOrdersService {

    private final OrderRepository orderRepository;
    private final ProductFeignClient productFeignClient;
    private final UserFeignClient userFeignClient;

    public OrdersServiceImpl(OrderRepository orderRepository, ProductFeignClient productFeignClient, UserFeignClient userFeignClient) {
        this.orderRepository = orderRepository;
        this.productFeignClient = productFeignClient;
        this.userFeignClient = userFeignClient;
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "addOrderFallback")
    @Transactional
    @Override
    public void addOrder(OrderDTO orderDTO) {

        if (orderDTO.getOrdererIdNo() != null && !orderDTO.getOrdererIdNo().isEmpty()) {
            UserDTO userDTO = userFeignClient.getUserByRsaId(orderDTO.getOrdererIdNo());

            if (!userDTO.getRsaId().equals(orderDTO.getOrdererIdNo())) {
                throw new OrderNotFoundException("Orderer/User does not exist in the system. Verify the Id number or create a user...");
            }
        }

        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

        if (orderDTO.getOrdererIdNo() == null) {
            throw new OrderNotFoundException("Orderer Id number is required!!");
        } else if (StringUtils.isEmpty(orderDTO.getOrdererIdNo()) || orderDTO.getOrdererIdNo().length() != 13) {
            throw new OrderNotFoundException("Orderer Id is invalid. \n Id must be 13 digits");
        }

        if (orderDTO.getOrderItems() != null) {
            for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {

                ProductDTO productDTO = productFeignClient.getProduct(orderItemDTO.getProductName());

                if (productDTO != null) {
                    int availableQuantity = productDTO.getQuantity();
                    int requestedQuantity = orderItemDTO.getQuantity();
                    if (requestedQuantity <= availableQuantity) {
                        orderItemDTOs.add(orderItemDTO);

                        productDTO.setQuantity(availableQuantity - requestedQuantity);
                        productFeignClient.updateProduct(productDTO.getId(), productDTO);
                    } else {
                        throw new OrderNotFoundException("There are " + availableQuantity + " " + productDTO.getProductName() + " in stock!");
                    }
                } else {
                    throw new OrderNotFoundException("There are no products with name: " + orderItemDTO.getProductName());
                }
            }
        }

        String orderNumber = orderDTO.getOrderNumber();
        if (orderNumber == null) {
            Random random = new Random();
            int randomNumber = 10000 + random.nextInt(90000);
            orderDTO.setOrderNumber(String.valueOf(randomNumber));
        }

        List<OrderItemDTO> orderItems = new ArrayList<>(orderItemDTOs);

        orderDTO.setOrderItems(orderItems);

        orderRepository.save(OrderMapper.ORDER_MAPPER.orderDtoToOrder(orderDTO));
    }

    @Transactional
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
        return OrderMapper.ORDER_MAPPER.orderToOrderDto(
            orderRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new OrderNotFoundException("Id", id))
        );
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
    public List<OrderDTO> getOrdersByQuantity(Integer quantity) {
        List<OrderDTO> orders = OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByQuantity(quantity));
        if (orders == null || orders.isEmpty()) {
            throw new OrdersNotFoundException("Quantity", quantity);
        }
        return orders;
    }

    @Override
    public OrderDTO getOrderByOrdererIdNo(String ordererIdNo) {
        return OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByOrdererIdNo(ordererIdNo));
    }

    @Transactional
    @Override
    public void updateOrder(UUID id, OrderDTO updatedOrderDTO) {
        OrderDTO existingOrder = getOrderById(id);

        if (updatedOrderDTO != null) {
            if (updatedOrderDTO.getOrderNumber() != null && !updatedOrderDTO.getOrderNumber().isEmpty()) {
                existingOrder.setOrderNumber(updatedOrderDTO.getOrderNumber());
            }

            if (updatedOrderDTO.getOrdererFullName() != null && !updatedOrderDTO.getOrdererFullName().isEmpty()) {
                existingOrder.setOrdererFullName(updatedOrderDTO.getOrdererFullName());
            }

            if (updatedOrderDTO.getOrdererIdNo() != null && !updatedOrderDTO.getOrdererIdNo().isEmpty()) {
                existingOrder.setOrdererIdNo(updatedOrderDTO.getOrdererIdNo());
            }

            if (updatedOrderDTO.getOrderItems() != null && !updatedOrderDTO.getOrderItems().isEmpty()) {
                List<OrderItemDTO> updatedOrderItems = new ArrayList<>();
                for (OrderItemDTO orderItemDTO : updatedOrderDTO.getOrderItems()) {
                    ProductDTO productDTO = productFeignClient.getProduct(orderItemDTO.getProductName());
                    if (productDTO != null) {
                        int availableQuantity = productDTO.getQuantity();
                        int requestedQuantity = orderItemDTO.getQuantity();
                        if (requestedQuantity <= availableQuantity) {
                            updatedOrderItems.add(orderItemDTO);
                        } else {
                            throw new OrderNotFoundException("There are " + availableQuantity + " " + productDTO.getProductName() + " in stock!");
                        }
                    } else {
                        throw new OrderNotFoundException("There are no products with name: " + orderItemDTO.getProductName());
                    }
                }
                existingOrder.setOrderItems(updatedOrderItems);
            }

            orderRepository.save(OrderMapper.ORDER_MAPPER.orderDtoToOrder(existingOrder));
        }
    }

    @Override
    public List<OrderDTO> searchOrders(UUID id, String orderNumber, Integer quantity) {
        List<OrderDTO> orders = new ArrayList<>();
        if (id != null) {
            orderRepository.findById(String.valueOf(id))
                .map(OrderMapper.ORDER_MAPPER::orderToOrderDto)
                .ifPresent(orders::add);
        }
        if (orderNumber != null && !orderNumber.isEmpty()) {
            orders.addAll(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByOrderNumber(orderNumber)));
        }
        if (quantity != null) {
            orders.addAll(OrderMapper.ORDER_MAPPER.orderToOrderDto(orderRepository.findByQuantity(quantity)));
        }

        return orders;
    }

    public void addOrderFallback(OrderDTO orderDTO, Throwable t) {
        log.error("Circuit breaker triggered for addOrder. Downstream service unavailable: {}", t.getMessage());
        throw new OrderNotFoundException("Order service is temporarily unavailable. Please try again later.");
    }
}
