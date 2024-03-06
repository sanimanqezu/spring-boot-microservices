package za.co.example.controllers;

import com.example.orders_service.api.OrdersApi;
import com.example.orders_service.models.OrderDTO;
import com.example.orders_service.models.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.threeten.bp.LocalDateTime;
import za.co.example.core.services.IOrdersService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class OrdersController implements OrdersApi{

    private final Logger logger = LoggerFactory.getLogger(OrdersController.class);

    private final IOrdersService ordersService;

    @Override
    public ResponseEntity<Void> addOrder(OrderDTO body) {
        logger.info("Adding order: {}", body.toString());
        ordersService.addOrder(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        logger.info("Retrieving all orders");
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @Override
    public ResponseEntity<OrderDTO> getOrderById(UUID id) {
        logger.info("Retrieving a order by id: {}", id);
        return ResponseEntity.ok(ordersService.getOrderById(id));
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getOrderByNumber(String orderNumber) {
        logger.info("Retrieving order(s) by order number: {}", orderNumber);
        return ResponseEntity.ok(ordersService.getOrdersByNumber(orderNumber));
    }

    @Override
    public ResponseEntity<OrderDTO> getOrderByOrdererIdNo(String ordererIdNo) {
        return ResponseEntity.ok(ordersService.getOrderByOrdererIdNo(ordererIdNo));
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getOrderByProductName(String productName) {
        return ResponseEntity.ok(ordersService.getOrderByProductName(productName));
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getOrderByQuantity(Integer quantity) {
        logger.info("Retrieving order(s) by quantity: {}", quantity);
        return ResponseEntity.ok(ordersService.getOrdersByQuality(quantity));
    }

    @Override
    public ResponseEntity<Void> removeOrderById(UUID id) {
        logger.info("Removing a order by Id: {}", id);
        ordersService.removeOrder(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> updateOrderById(UUID id, OrderDTO body) {
        logger.info("Updating order identified by id: {}, with order: {}", id, body);
        ordersService.updateOrder(id, body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> searchOrders(UUID id, String orderNumber, Integer quantity, String product) {
        logger.info("Searching user(s) by the following properties: \n Id: {} \n Order Number: {} \n Quantity: {} \n Product: {} ",
                id, orderNumber, quantity, product);
        return ResponseEntity.ok(ordersService.searchOrders(id, orderNumber, quantity, product));
    }
}
