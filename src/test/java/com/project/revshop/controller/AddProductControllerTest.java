package com.project.revshop.controller;


import com.project.revshop.entity.Category;
import com.project.revshop.entity.Product;
import com.project.revshop.entity.SellerModel;
import com.project.revshop.entity.Size;
import com.project.revshop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AddProductControllerTest {

    @InjectMocks
    private AddProductController addProductController;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSizes() {
        int categoryId = 1;
        List<Size> sizes = new ArrayList<>();
        sizes.add(new Size());
        when(productService.getAllSizes(categoryId)).thenReturn(sizes);

        List<Size> result = addProductController.getAllSizes(categoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productService, times(1)).getAllSizes(categoryId);
    }

    @Test
    void testShowUpdateForm() {
        int productId = 1;
        Product product = new Product();
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());

        when(productService.getProductDetails(productId)).thenReturn(product);
        when(productService.getAllCategories()).thenReturn(categories);

        String viewName = addProductController.showUpdateForm(productId, model);

        assertEquals("updateProductToInventory", viewName);
        verify(model, times(1)).addAttribute("categories", categories);
        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void testShowProductPage() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category());

        when(productService.getAllCategories()).thenReturn(categories);

        String viewName = addProductController.showProductPage(model);

        assertEquals("addProductsToInventory", viewName);
        verify(model, times(1)).addAttribute(eq("Product"), any(Product.class));
        verify(model, times(1)).addAttribute("categories", categories);
    }

    @Test
    void testUpdateProductToInventory() {
        int productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        when(session.getAttribute("sellerid")).thenReturn(1);
        SellerModel sellerModel = new SellerModel();
        when(productService.getSellerById(1)).thenReturn(sellerModel);
        
        String redirectUrl = addProductController.updateProductToInventory(product, model, productId, session);

        assertEquals("redirect:/api/v1/seller/products", redirectUrl);
        verify(productService, times(1)).saveProduct(product);
        assertEquals(sellerModel, product.getSellerModel());
    }

    @Test
    void testAddProductToInventory() {
        Product product = new Product();
        int sizeId = 1;
        when(session.getAttribute("sellerid")).thenReturn(1);
        Size size = new Size();
        SellerModel sellerModel = new SellerModel();

        when(productService.getSizeById(sizeId)).thenReturn(size);
        when(productService.getSellerById(1)).thenReturn(sellerModel);

        String redirectUrl = addProductController.addProductToInventory(product, model, sizeId, session);

        assertEquals("redirect:/api/v1/seller/products", redirectUrl);
        verify(productService, times(1)).saveProduct(product);
        assertEquals(size, product.getSize());
        assertEquals(sellerModel, product.getSellerModel());
    }

    @Test
    void testAddProductToInventorySellerNotFound() {
        Product product = new Product();
        int sizeId = 1;
        when(session.getAttribute("sellerid")).thenReturn(1);
        when(productService.getSizeById(sizeId)).thenReturn(new Size());
        when(productService.getSellerById(1)).thenReturn(null);

        String redirectUrl = addProductController.addProductToInventory(product, model, sizeId, session);

        assertEquals("redirect:/api/v1/login", redirectUrl);
    }

    @Test
    void testDeleteProductFromInventory() {
        int productId = 1;
        when(session.getAttribute("sellerid")).thenReturn(1);
        when(productService.getSellerById(1)).thenReturn(new SellerModel());

        String redirectUrl = addProductController.deleteProuctFromInventory(productId, session);

        assertEquals("redirect:/api/v1/seller/products", redirectUrl);
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void testDeleteProductFromInventorySellerNotFound() {
        int productId = 1;
        when(session.getAttribute("sellerid")).thenReturn(1);
        when(productService.getSellerById(1)).thenReturn(null);

        String redirectUrl = addProductController.deleteProuctFromInventory(productId, session);

        assertEquals("redirect:/api/v1/login", redirectUrl);
    }
}
