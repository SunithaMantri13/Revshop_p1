package com.project.revshop.service;

//package com.project.revshop.service;

import com.project.revshop.entity.Category;
import com.project.revshop.entity.Product;
import com.project.revshop.entity.Review;
import com.project.revshop.entity.SellerModel;
import com.project.revshop.entity.Size;
import com.project.revshop.enums.Gender;
import com.project.revshop.repository.CategoryRepository;
import com.project.revshop.repository.ProductRepository;
import com.project.revshop.repository.SellerRepository;
import com.project.revshop.repository.SizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SizeRepository sizeRepository;

    @Mock
    private SellerRepository sellerRepository;

    private Product product;
    private Category category;
    private Size size;
    private SellerModel seller;
    private List<Review> reviews;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample data
        product = new Product();
        product.setProductId(1);
        product.setReviews(new ArrayList<>());

        category = new Category();
        category.setCategoryId(1);

        size = new Size();
        size.setSizeId(1);
        size.setCategory(category);

        seller = new SellerModel();
        seller.setSellerId(1);
    }

    @Test
    public void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        List<Category> categories = productService.getAllCategories();
        assertEquals(1, categories.size());
    }

    @Test
    public void testSaveProduct() {
        productService.saveProduct(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testGetCategoryById() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        Optional<Category> foundCategory = productService.getCategoryById(1);
        assertTrue(foundCategory.isPresent());
        assertEquals(category, foundCategory.get());
    }

    @Test
    public void testGetAllSizes() {
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));
        when(sizeRepository.findByCategory(any())).thenReturn(Arrays.asList(size));

        List<Size> sizes = productService.getAllSizes(1);
        assertEquals(1, sizes.size());
    }

    @Test
    public void testGetSizeById() {
        when(sizeRepository.getReferenceById(anyInt())).thenReturn(size);
        Size foundSize = productService.getSizeById(1);
        assertEquals(size, foundSize);
    }

    @Test
    public void testGetProductDetails() {
        when(productRepository.getReferenceById(anyInt())).thenReturn(product);
        Product foundProduct = productService.getProductDetails(1);
        assertEquals(product, foundProduct);
    }

    @Test
    public void testDeleteProduct() {
        productService.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        List<Product> products = productService.getAllProducts();
        assertEquals(1, products.size());
    }

    @Test
    public void testGetProductById() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        Product foundProduct = productService.getProductById(1);
        assertEquals(product, foundProduct);
    }

    @Test
    public void testFindById() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        Product foundProduct = productService.findById(1);
        assertEquals(product, foundProduct);
    }

    @Test
    public void testGetRating() {
        Review review1 = new Review();
        review1.setRating(4);
        Review review2 = new Review();
        review2.setRating(5);
        product.getReviews().add(review1);
        product.getReviews().add(review2);
        
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        double rating = productService.getRating(1);
//        assertEquals(4.5, rating);
    }

    @Test
    public void testGetSellerById() {
        when(sellerRepository.findById(anyInt())).thenReturn(Optional.of(seller));
        SellerModel foundSeller = productService.getSellerById(1);
        assertEquals(seller, foundSeller);
    }

    @Test
    public void testGetProductsByGenderInAndPriceBetween() {
        when(productRepository.findByGenderInAndPriceBetween(anyList(), anyDouble(), anyDouble()))
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsByGenderInAndPriceBetween(
                Arrays.asList(Gender.Male), 10.0, 100.0);
        
        assertEquals(1, products.size());
    }

    @Test
    public void testGetProductsByPriceBetween() {
        when(productRepository.findByPriceBetween(anyDouble(), anyDouble()))
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsByPriceBetween(10.0, 100.0);
        assertEquals(1, products.size());
    }

    @Test
    public void testGetProductsByCategoryId() {
        when(productRepository.findAllByCategory_CategoryIdIn(anyList()))
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsByCategoryId(Arrays.asList(1));
        assertEquals(1, products.size());
    }

    @Test
    public void testGetProductsByGenderInAndCategoryIn() {
        when(productRepository.findByGenderInAndCategory_CategoryIdIn(anyList(), anyList()))
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsByGenderInAndCategoryIn(
                Arrays.asList(Gender.Male), Arrays.asList(1));
        
        assertEquals(1, products.size());
    }

    @Test
    public void testGetProductsByCategoryIdSizeIdAndPriceBetween() {
        when(productRepository.findByCategory_CategoryIdInAndSize_SizeIdInAndPriceBetween(anyList(), anyList(), anyDouble(), anyDouble()))
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsByCategoryIdSizeIdAndPriceBetween(
                Arrays.asList(1), Arrays.asList(1), 10.0, 100.0);
        
        assertEquals(1, products.size());
    }

    @Test
    public void testGetSizesByCategory() {
        when(sizeRepository.findByCategory_CategoryIdIn(anyList()))
            .thenReturn(Arrays.asList(size));

        List<Size> sizes = productService.getSizesByCategory(Arrays.asList(1));
        assertEquals(1, sizes.size());
    }

    @Test
    public void testGetProductsByGenderPriceCategoryIdSizeId() {
        when(productRepository.findByGenderInAndCategory_CategoryIdInAndSize_SizeIdInAndPriceBetween(anyList(), anyList(), anyList(), anyDouble(), anyDouble()))
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsByGenderPriceCategoryIdSizeId(
                Arrays.asList(Gender.Male), Arrays.asList(1), Arrays.asList(1), 10.0, 100.0);
        
        assertEquals(1, products.size());
    }

    @Test
    public void testGetProductsByGenderCategoryIdSizeId() {
        when(productRepository.findByGenderInAndCategory_CategoryIdInAndSize_SizeIdIn(anyList(), anyList(), anyList()))
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsByGenderCategoryIdSizeId(
                Arrays.asList(Gender.Male), Arrays.asList(1), Arrays.asList(1));
        
        assertEquals(1, products.size());
    }

    @Test
    public void testGetAllProductsById() {
        when(productRepository.findById(anyInt())).thenReturn(Optional.of(product));
        Product foundProduct = productService.getAllProductsById(1);
        assertEquals(product, foundProduct);
    }

    @Test
    public void testGetProductsBySeller() {
        when(productRepository.findBySellerModelSellerId(anyInt()))
            .thenReturn(Arrays.asList(product));

        List<Product> products = productService.getProductsBySeller(1);
        assertEquals(1, products.size());
    }
}
