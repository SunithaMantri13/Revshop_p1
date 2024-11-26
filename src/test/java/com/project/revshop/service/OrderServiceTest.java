package com.project.revshop.service;

import com.project.revshop.entity.*;
import com.project.revshop.repository.OrderItemRepository;
import com.project.revshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    private UserModel user;
    private Cart cartItem;
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserModel();
        user.setUserId(1); // Example user ID

        Product product = new Product();
        product.setProductId(1);
        product.setPrice(100.0); // Example product price

        cartItem = new Cart();
        cartItem.setProduct(product);
        cartItem.setQuantity(2); // Example quantity

        order = new Order();
        order.setOrderId(1);
        order.setUserModel(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(200.0); // Example total amount
    }

    @Test
    void testCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.saveAll(anyList())).thenReturn(Collections.singletonList(new OrderItem()));

        List<Cart> cartItems = Collections.singletonList(cartItem);
        Order createdOrder = orderService.createOrder(user, cartItems, "Shipping Address", "Billing Address");

        assertNotNull(createdOrder);
        assertEquals(200.0, createdOrder.getTotalAmount());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testGetOrdersByUser() {
        when(orderRepository.findByUserModelOrderByOrderDateDesc(user)).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.getOrdersByUser(user);

        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
    }

    @Test
    void testGetOrderItemsByOrder() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());

        when(orderItemRepository.findByOrder(order)).thenReturn(Collections.singletonList(orderItem));

        List<OrderItem> orderItems = orderService.getOrderItemsByOrder(order);

        assertEquals(1, orderItems.size());
        assertEquals(orderItem, orderItems.get(0));
    }

    @Test
    void testGetOrdersForSeller() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

        List<Order> orders = orderService.getOrdersForSeller();

        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1)).thenReturn(java.util.Optional.of(order));

        Order foundOrder = orderService.getOrderById(1);

        assertNotNull(foundOrder);
        assertEquals(order, foundOrder);
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1)).thenReturn(java.util.Optional.empty());

        Order foundOrder = orderService.getOrderById(1);

        assertNull(foundOrder);
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateOrderStatus(order, Order.OrderStatus.Shipped);

        assertEquals(Order.OrderStatus.Shipped, order.getOrderStatus());
        verify(orderRepository, times(1)).save(order);
    }
}
