package com.project.revshop.service;


import com.project.revshop.entity.Product;
import com.project.revshop.entity.UserModel;
import com.project.revshop.entity.Wishlist;
import com.project.revshop.repository.ProductRepository;
import com.project.revshop.repository.UserRepository;
import com.project.revshop.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WishlistServiceTest {

    @InjectMocks
    private WishlistService wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    private UserModel user;
    private Product product;
    private Wishlist wishlist;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserModel();
        user.setUserId(1); // Example user ID

        product = new Product();
        product.setProductId(1); // Example product ID

        wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
    }

    @Test
    void testAddToWishlist_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(wishlistRepository.findByUserUserIdAndProductProductId(1, 1)).thenReturn(null);

        wishlistService.addToWishlist(1, 1);

        verify(wishlistRepository, times(1)).save(any(Wishlist.class));
    }

    @Test
    void testAddToWishlist_ProductAlreadyInWishlist() {
        when(wishlistRepository.findByUserUserIdAndProductProductId(1, 1)).thenReturn(wishlist);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.addToWishlist(1, 1);
        });

        assertEquals("Product already in wishlist", exception.getMessage());
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    void testRemoveFromWishlist_Success() {
        when(wishlistRepository.findByUserUserIdAndProductProductId(1, 1)).thenReturn(wishlist);

        wishlistService.removeFromWishlist(1, 1);

        verify(wishlistRepository, times(1)).delete(wishlist);
    }

    @Test
    void testRemoveFromWishlist_ProductNotInWishlist() {
        when(wishlistRepository.findByUserUserIdAndProductProductId(1, 1)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.removeFromWishlist(1, 1);
        });

        assertEquals("Product not in wishlist", exception.getMessage());
        verify(wishlistRepository, never()).delete(any(Wishlist.class));
    }

    @Test
    void testGetWishlist() {
        when(wishlistRepository.findByUserUserId(1)).thenReturn(Collections.singletonList(wishlist));

        List<Product> result = wishlistService.getWishlist(1);

        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void testGetWishlist_Empty() {
        when(wishlistRepository.findByUserUserId(1)).thenReturn(Collections.emptyList());

        List<Product> result = wishlistService.getWishlist(1);

        assertTrue(result.isEmpty());
    }
}
