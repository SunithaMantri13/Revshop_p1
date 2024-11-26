package com.project.revshop.controller;
import com.project.revshop.entity.Category;
import com.project.revshop.entity.Product;
import com.project.revshop.service.ProductService;
import com.project.revshop.service.WishlistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ViewProductsControllerTest {

    @InjectMocks
    private ViewProductsController viewProductsController;

    @Mock
    private ProductService productService;

    @Mock
    private WishlistService wishlistService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewDashboard() {
        String result = viewProductsController.viewDashboard();
        assertEquals("buyerDashboard", result, "The view dashboard should return the correct view name");
    }

    @Test
    void testViewProductsPage() {
        // Mock data
        List<Product> products = Arrays.asList(new Product(), new Product());
        List<Category> categories = Arrays.asList(new Category());
        Integer userId = 1;
        List<Product> wishlistProducts = Arrays.asList(new Product());

        // Mock service calls
        when(productService.getAllProducts()).thenReturn(products);
        when(productService.getAllCategories()).thenReturn(categories);
        when(session.getAttribute("userId")).thenReturn(userId);
        when(wishlistService.getWishlist(userId)).thenReturn(wishlistProducts);

        String result = viewProductsController.viewProductsPage(session, model);

        // Verify interactions
        verify(model).addAttribute("categories", categories);
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("wishlistProducts", wishlistProducts);
        
        assertEquals("view001", result, "The view products page should return the correct view name");
    }

    @Test
    void testViewProductsById() {
        int productId = 1;
        Product product = new Product();
        
        // Mock service calls
        when(productService.getProductById(productId)).thenReturn(product);
        
        String result = viewProductsController.viewProductsById(productId, model);
        
        // Verify interactions
        verify(model).addAttribute("product", product);
        
        assertEquals("ProductDetails", result, "The view product by ID should return the correct view name");
    }
}
