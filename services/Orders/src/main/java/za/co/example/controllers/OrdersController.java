package za.co.example.controllers;

import com.example.orders_service.api.OrdersApi;
import com.example.orders_service.models.OrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import za.co.example.core.services.IOrdersService;

import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RestController
public class OrdersController implements OrdersApi{

    private final IOrdersService ordersService;

    @Override
    public ResponseEntity<Void> addOrder(OrderDTO body) {
        log.info("Adding order: {}", body.toString());
        ordersService.addOrder(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("Retrieving all orders");
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @Override
    public ResponseEntity<OrderDTO> getOrderById(UUID id) {
        log.info("Retrieving a order by id: {}", id);
        return ResponseEntity.ok(ordersService.getOrderById(id));
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getOrderByNumber(String orderNumber) {
        log.info("Retrieving order(s) by order number: {}", orderNumber);
        return ResponseEntity.ok(ordersService.getOrdersByNumber(orderNumber));
    }

    @Override
    public ResponseEntity<OrderDTO> getOrderByOrdererIdNo(String ordererIdNo) {
        return ResponseEntity.ok(ordersService.getOrderByOrdererIdNo(ordererIdNo));
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getOrderByQuantity(Integer quantity) {
        log.info("Retrieving order(s) by quantity: {}", quantity);
        return ResponseEntity.ok(ordersService.getOrdersByQuantity(quantity));
    }

    @Override
    public ResponseEntity<Void> removeOrderById(UUID id) {
        log.info("Removing a order by Id: {}", id);
        ordersService.removeOrder(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> updateOrderById(UUID id, OrderDTO body) {
        log.info("Updating order identified by id: {}, with order: {}", id, body);
        ordersService.updateOrder(id, body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> searchOrders(UUID id, String orderNumber, Integer quantity) {
        log.info("Searching orders by: Id={}, OrderNumber={}, Quantity={}", id, orderNumber, quantity);
        return ResponseEntity.ok(ordersService.searchOrders(id, orderNumber, quantity));
    }
}
