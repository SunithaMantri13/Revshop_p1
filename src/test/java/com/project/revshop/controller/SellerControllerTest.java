package com.project.revshop.controller;


import com.project.revshop.entity.Product;
import com.project.revshop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SellerControllerTest {

    @InjectMocks
    private SellerController sellerController;

    @Mock
    private ProductService productService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowSeller() {
        String viewName = sellerController.showSeller();
        assertEquals("sellerDashboard", viewName);
    }

    @Test
    void testShowProducts() {
        int sellerId = 1;
        when(session.getAttribute("sellerid")).thenReturn(sellerId);
        
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        when(productService.getProductsBySeller(sellerId)).thenReturn(products);

        String viewName = sellerController.showProducts(session, model);

        assertEquals("sellerProducts", viewName);
        verify(model, times(1)).addAttribute("products", products);
    }

    @Test
    void testShowProductsWhenNoProductsFound() {
        int sellerId = 1;
        when(session.getAttribute("sellerid")).thenReturn(sellerId);
        
        when(productService.getProductsBySeller(sellerId)).thenReturn(new ArrayList<>());

        String viewName = sellerController.showProducts(session, model);

        assertEquals("sellerProducts", viewName);
        verify(model, times(1)).addAttribute("products", new ArrayList<>());
    }
}
