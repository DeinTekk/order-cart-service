package com.programthis.order_cart_service.controller;

import com.programthis.order_cart_service.model.Order;
import com.programthis.order_cart_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrderFromCart_Success() {
        Long userId = 1L;
        OrderController.OrderCreationRequest request = new OrderController.OrderCreationRequest("Fake Street", "Credit Card");
        Order mockOrder = new Order();
        when(orderService.createOrderFromCart(userId, request.getShippingAddress(), request.getPaymentMethod()))
                .thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.createOrderFromCart(userId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService).createOrderFromCart(userId, "Fake Street", "Credit Card");
    }

    @Test
    public void testCreateOrderFromCart_Failure() {
        Long userId = 1L;
        OrderController.OrderCreationRequest request = new OrderController.OrderCreationRequest("Fake Street", "Credit Card");

        when(orderService.createOrderFromCart(anyLong(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Cart not found"));

        ResponseEntity<Order> response = orderController.createOrderFromCart(userId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetOrderById_Found() {
        Long orderId = 1L;
        Order mockOrder = new Order();
        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(mockOrder));

        ResponseEntity<Order> response = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
    }

    @Test
    public void testGetOrderById_NotFound() {
        Long orderId = 1L;
        when(orderService.getOrderById(orderId)).thenReturn(Optional.empty());

        ResponseEntity<Order> response = orderController.getOrderById(orderId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetOrdersByUserId() {
        Long userId = 1L;
        List<Order> mockOrders = Arrays.asList(new Order(), new Order());
        when(orderService.getOrdersByUserId(userId)).thenReturn(mockOrders);

        ResponseEntity<List<Order>> response = orderController.getOrdersByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrders, response.getBody());
    }

    @Test
    public void testUpdateOrderStatus_Success() {
        Long orderId = 1L;
        String newStatus = "SHIPPED";
        Order updatedOrder = new Order();
        when(orderService.updateOrderStatus(orderId, newStatus)).thenReturn(updatedOrder);

        ResponseEntity<Order> response = orderController.updateOrderStatus(orderId, newStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrder, response.getBody());
    }

    @Test
    public void testUpdateOrderStatus_Failure() {
        Long orderId = 1L;
        String newStatus = "SHIPPED";
        when(orderService.updateOrderStatus(orderId, newStatus))
                .thenThrow(new RuntimeException("Order not found"));

        ResponseEntity<Order> response = orderController.updateOrderStatus(orderId, newStatus);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteOrder_Success() {
        Long orderId = 1L;

        doNothing().when(orderService).deleteOrder(orderId);

        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteOrder_NotFound() {
        Long orderId = 1L;
        doThrow(new RuntimeException("Order not found")).when(orderService).deleteOrder(orderId);

        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
