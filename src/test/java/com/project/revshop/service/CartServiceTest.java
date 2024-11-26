package com.project.revshop.service;


import com.project.revshop.entity.Cart;
import com.project.revshop.entity.Product;
import com.project.revshop.entity.UserModel;
import com.project.revshop.repository.CartRepository;
import com.project.revshop.repository.ProductRepository;
import com.project.revshop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    private UserModel user;
    private Product product;
    private Cart cartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserModel();
        user.setUserId(1);
        product = new Product();
        product.setProductId(1);
        cartItem = new Cart();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
    }

    @Test
    public void testAddToCart_NewItem() {
        when(cartRepository.findByUserModelAndProduct(any(UserModel.class), any(Product.class))).thenReturn(Optional.empty());

        cartService.addToCart(cartItem);

        verify(cartRepository, times(1)).save(cartItem);
    }

    @Test
    public void testAddToCart_ExistingItem() {
        Cart existingCartItem = new Cart();
        existingCartItem.setQuantity(2);
        when(cartRepository.findByUserModelAndProduct(any(UserModel.class), any(Product.class))).thenReturn(Optional.of(existingCartItem));

        cartService.addToCart(cartItem);

        assertEquals(3, existingCartItem.getQuantity());
        verify(cartRepository, times(1)).save(existingCartItem);
    }

    @Test
    public void testGetCartItemsByUserModel() {
        List<Cart> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        when(cartRepository.findByUserModel(any(UserModel.class))).thenReturn(cartItems);

        List<Cart> result = cartService.getCartItemsByuserModel(user);

        assertEquals(1, result.size());
        assertEquals(cartItem, result.get(0));
        verify(cartRepository, times(1)).findByUserModel(user);
    }

    @Test
    public void testClearCart() {
        cartService.clearCart(user);
        verify(cartRepository, times(1)).deleteByUserModel(user);
    }

    @Test
    public void testFindByUserAndProduct() {
        when(cartRepository.findByUserModelUserIdAndProductProductId(anyInt(), anyInt())).thenReturn(cartItem);

        Cart result = cartService.findByUserAndProduct(user.getUserId(), product.getProductId());

        assertEquals(cartItem, result);
        verify(cartRepository, times(1)).findByUserModelUserIdAndProductProductId(user.getUserId(), product.getProductId());
    }

    @Test
    public void testDeleteFromCart() {
        cartService.deleteFromCart(cartItem);
        verify(cartRepository, times(1)).delete(cartItem);
    }

    @Test
    public void testUpdateCart() {
        cartService.updateCart(cartItem);
        verify(cartRepository, times(1)).save(cartItem);
    }
}

