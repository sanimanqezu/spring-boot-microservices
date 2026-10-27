package za.co.example.controllers;

import com.example.orders_service.models.OrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import za.co.example.core.services.IOrdersService;
import za.co.example.exceptions.OrderNotFoundException;
import za.co.example.exceptions.OrdersNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IOrdersService ordersService;

    @Test
    void addOrder_returns201_andDelegatesToService() throws Exception {
        OrderDTO body = new OrderDTO()
                .ordererIdNo("1234567890123")
                .ordererFullName("John Doe")
                .orderNumber("12345")
                .quantity(2);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        verify(ordersService, times(1)).addOrder(any(OrderDTO.class));
    }

    @Test
    void getAllOrders_returns200AndBody() throws Exception {
        when(ordersService.getAllOrders())
                .thenReturn(List.of(new OrderDTO().orderNumber("12345").quantity(2)));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderNumber").value("12345"));
    }

    @Test
    void getOrderById_returns200_whenFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(ordersService.getOrderById(id))
                .thenReturn(new OrderDTO().id(id).orderNumber("12345"));

        mockMvc.perform(get("/orders/id").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("12345"));
    }

    @Test
    void getOrderById_returns404_whenServiceThrowsOrderNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(ordersService.getOrderById(id))
                .thenThrow(new OrderNotFoundException("Order not found"));

        mockMvc.perform(get("/orders/id").param("id", id.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Order not found"));
    }

    @Test
    void getAllOrders_returns404_whenServiceThrowsOrdersNotFound() throws Exception {
        when(ordersService.getAllOrders())
                .thenThrow(new OrdersNotFoundException("No order was found"));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No order was found"));
    }

    @Test
    void getOrderById_returns400_whenIdParamMissing() throws Exception {
        mockMvc.perform(get("/orders/id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeOrderById_returns200_andDelegatesToService() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/orders/id").param("id", id.toString()))
                .andExpect(status().isOk());

        verify(ordersService, times(1)).removeOrder(eq(id));
    }

    @Test
    void removeOrderById_returns404_whenServiceThrowsOrderNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new OrderNotFoundException("Order not found"))
                .when(ordersService).removeOrder(id);

        mockMvc.perform(delete("/orders/id").param("id", id.toString()))
                .andExpect(status().isNotFound());
    }
}
