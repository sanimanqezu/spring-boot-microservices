package za.co.example.core.impl;

import com.example.orders_service.models.OrderDTO;
import com.example.orders_service.models.OrderItemDTO;
import com.example.orders_service.models.ProductDTO;
import com.example.orders_service.models.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.example.exceptions.OrderNotFoundException;
import za.co.example.exceptions.OrdersNotFoundException;
import za.co.example.persistance.entities.Order;
import za.co.example.persistance.repositories.OrderRepository;
import za.co.example.proxy.ProductFeignClient;
import za.co.example.proxy.UserFeignClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductFeignClient productFeignClient;

    @Mock
    private UserFeignClient userFeignClient;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    private Order order;
    private String id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID().toString();
        order = new Order();
        order.setId(id);
        order.setOrderNumber("12345");
        order.setQuantity(2);
        order.setOrdererIdNo("1234567890123");
        order.setOrdererFullName("John Doe");
    }

    @Test
    void addOrder_withNullOrdererIdNo_throwsOrderNotFoundException() {
        OrderDTO dto = new OrderDTO().ordererIdNo(null);

        assertThatThrownBy(() -> ordersService.addOrder(dto))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Orderer Id number is required");
    }

    @Test
    void addOrder_withShortOrdererIdNo_throwsOrderNotFoundException() {
        OrderDTO dto = new OrderDTO().ordererIdNo("123");

        assertThatThrownBy(() -> ordersService.addOrder(dto))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Orderer Id is invalid");
    }

    @Test
    void addOrder_withValidDataAndNoItems_savesOrder() {
        UserDTO user = new UserDTO().rsaId("1234567890123");
        when(userFeignClient.getUserByRsaId("1234567890123")).thenReturn(user);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO dto = new OrderDTO()
                .ordererIdNo("1234567890123")
                .ordererFullName("John Doe")
                .orderNumber("12345");

        ordersService.addOrder(dto);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void addOrder_whenProductNotFound_throwsOrderNotFoundException() {
        UserDTO user = new UserDTO().rsaId("1234567890123");
        when(userFeignClient.getUserByRsaId("1234567890123")).thenReturn(user);
        when(productFeignClient.getProduct("NonExistentProduct")).thenReturn(null);

        OrderItemDTO item = new OrderItemDTO().productName("NonExistentProduct").quantity(1);
        OrderDTO dto = new OrderDTO()
                .ordererIdNo("1234567890123")
                .orderItems(List.of(item));

        assertThatThrownBy(() -> ordersService.addOrder(dto))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("There are no products with name: NonExistentProduct");
    }

    @Test
    void addOrder_whenInsufficientStock_throwsOrderNotFoundException() {
        UserDTO user = new UserDTO().rsaId("1234567890123");
        when(userFeignClient.getUserByRsaId("1234567890123")).thenReturn(user);

        ProductDTO product = new ProductDTO().productName("Widget").quantity(1);
        when(productFeignClient.getProduct("Widget")).thenReturn(product);

        OrderItemDTO item = new OrderItemDTO().productName("Widget").quantity(5);
        OrderDTO dto = new OrderDTO()
                .ordererIdNo("1234567890123")
                .orderItems(List.of(item));

        assertThatThrownBy(() -> ordersService.addOrder(dto))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("in stock");
    }

    @Test
    void removeOrder_withExistingId_deletesOrder() {
        when(orderRepository.existsById(id)).thenReturn(true);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        ordersService.removeOrder(UUID.fromString(id));

        verify(orderRepository, times(1)).delete(any(Order.class));
    }

    @Test
    void removeOrder_withNonExistingId_throwsOrderNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(orderRepository.existsById(randomId.toString())).thenReturn(false);

        assertThatThrownBy(() -> ordersService.removeOrder(randomId))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void getAllOrders_withOrders_returnsList() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderDTO> result = ordersService.getAllOrders();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAllOrders_withNoOrders_throwsOrdersNotFoundException() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> ordersService.getAllOrders())
                .isInstanceOf(OrdersNotFoundException.class)
                .hasMessageContaining("No order was found");
    }

    @Test
    void getOrderById_withExistingId_returnsOrder() {
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        OrderDTO result = ordersService.getOrderById(UUID.fromString(id));

        assertThat(result).isNotNull();
    }

    @Test
    void getOrderById_withNonExistingId_throwsOrderNotFoundException() {
        UUID randomId = UUID.randomUUID();
        when(orderRepository.findById(randomId.toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ordersService.getOrderById(randomId))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void getOrdersByQuantity_withResults_returnsList() {
        when(orderRepository.findByQuantity(2)).thenReturn(List.of(order));

        List<OrderDTO> result = ordersService.getOrdersByQuantity(2);

        assertThat(result).hasSize(1);
    }

    @Test
    void getOrdersByQuantity_withNoResults_throwsOrdersNotFoundException() {
        when(orderRepository.findByQuantity(999)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> ordersService.getOrdersByQuantity(999))
                .isInstanceOf(OrdersNotFoundException.class)
                .hasMessageContaining("Quantity");
    }
}
