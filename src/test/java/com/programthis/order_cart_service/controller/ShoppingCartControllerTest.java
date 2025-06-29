package com.programthis.order_cart_service.controller;

import com.programthis.order_cart_service.model.ShoppingCart;
import com.programthis.order_cart_service.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShoppingCartControllerTest {

    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    private ShoppingCart cart;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cart = new ShoppingCart();
        cart.setId(1L);
    }

    @Test
    public void testGetOrCreateCart() {
        when(shoppingCartService.getOrCreateShoppingCart(1L)).thenReturn(cart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.getOrCreateCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testAddProductToCart_Success() {
        when(shoppingCartService.addProductToCart(1L, 100L, 2)).thenReturn(cart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.addProductToCart(1L, 100L, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testAddProductToCart_Failure() {
        when(shoppingCartService.addProductToCart(1L, 100L, 2)).thenThrow(new RuntimeException("Product not found"));

        ResponseEntity<ShoppingCart> response = shoppingCartController.addProductToCart(1L, 100L, 2);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateProductQuantityInCart_Success() {
        when(shoppingCartService.updateProductQuantityInCart(1L, 100L, 3)).thenReturn(cart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.updateProductQuantityInCart(1L, 100L, 3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testUpdateProductQuantityInCart_Failure() {
        when(shoppingCartService.updateProductQuantityInCart(1L, 100L, 3)).thenThrow(new RuntimeException("Product not found"));

        ResponseEntity<ShoppingCart> response = shoppingCartController.updateProductQuantityInCart(1L, 100L, 3);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testRemoveProductFromCart_Success() {
        when(shoppingCartService.removeProductFromCart(1L, 100L)).thenReturn(cart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.removeProductFromCart(1L, 100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testRemoveProductFromCart_Failure() {
        when(shoppingCartService.removeProductFromCart(1L, 100L)).thenThrow(new RuntimeException("Product not in cart"));

        ResponseEntity<ShoppingCart> response = shoppingCartController.removeProductFromCart(1L, 100L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testClearCart_Success() {
        when(shoppingCartService.clearCart(1L)).thenReturn(cart);

        ResponseEntity<ShoppingCart> response = shoppingCartController.clearCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void testClearCart_Failure() {
        when(shoppingCartService.clearCart(1L)).thenThrow(new RuntimeException("Cart not found"));

        ResponseEntity<ShoppingCart> response = shoppingCartController.clearCart(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
