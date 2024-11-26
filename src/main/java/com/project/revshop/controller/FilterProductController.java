package com.project.revshop.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.revshop.entity.Category;
import com.project.revshop.entity.Product;
import com.project.revshop.entity.Size;
import com.project.revshop.enums.Gender;
import com.project.revshop.service.ProductService;
import com.project.revshop.service.WishlistService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/v1")
public class FilterProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/allfetchedproducts")
    public String showFilteredProducts(
            @RequestParam(required = false) List<String> gender,
            @RequestParam(required = false) List<String> price,
            @RequestParam(required = false) List<Integer> category,
            @RequestParam(required = false) List<Integer> size,
            HttpSession session,
            Model model) {
        
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);
        
        List<Integer> categoryId = category != null ? category : List.of();
        List<Integer> sizeId = size !=null ? size : List.of();
        
        List<Gender> selectedGenders = (gender != null && !gender.isEmpty()) 
                ? gender.stream().map(Gender::valueOf).collect(Collectors.toList()) 
                : List.of();
        
        double minPrice = 0.0;
        double maxPrice = Double.MAX_VALUE;

        if (price != null) {
            if (price.contains("under2000")) {
                maxPrice = 2000.00;
            }
            if (price.contains("2000to3500")) {
                minPrice = Math.max(minPrice, 2000.00);
                maxPrice = Math.min(maxPrice, 3500.00);
            }
            if (price.contains("3500to6000")) {
                minPrice = Math.max(minPrice, 3500.00);
                maxPrice = Math.min(maxPrice, 6000.00);
            }
            if (price.contains("above6000")) {
                minPrice = Math.max(minPrice, 6000.00);
            }
        }
        
        List<Product> fetchedProducts;
        
        if (!selectedGenders.isEmpty() && !categoryId.isEmpty() && !sizeId.isEmpty()) {
        	fetchedProducts = productService.getProductsByGenderPriceCategoryIdSizeId(selectedGenders, categoryId, sizeId, minPrice, maxPrice);
        	
        	  if(fetchedProducts.isEmpty()) {
                  // Fetch alternative products without the price filter and sort them
                  fetchedProducts = productService.getProductsByGenderCategoryIdSizeId(selectedGenders, categoryId, sizeId);
                   fetchedProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                  
                  // Add error message to the model
                  model.addAttribute("errorMessage", "No products available for the selected price range.");
        	  }
        } else if (!selectedGenders.isEmpty() && !categoryId.isEmpty()) {
        	fetchedProducts = productService.getProductsByGenderInAndCategoryIn(selectedGenders, categoryId);
        } else if (!selectedGenders.isEmpty()) {
        	fetchedProducts = productService.getProductsByGenderInAndPriceBetween(selectedGenders, minPrice, maxPrice);
        } else if (!categoryId.isEmpty() && !sizeId.isEmpty()) {
             fetchedProducts = productService.getProductsByCategoryIdSizeIdAndPriceBetween(categoryId, sizeId, minPrice, maxPrice);
            fetchedProducts = fetchedProducts.isEmpty() ? productService.getProductsByCategoryId(categoryId) : fetchedProducts;
        } else {
        	fetchedProducts = productService.getProductsByPriceBetween(minPrice, maxPrice);
        }
        
        int userId = (int) session.getAttribute("userId");
        List<Product> wishlistProducts = wishlistService.getWishlist(userId);
        model.addAttribute("wishlistProducts", wishlistProducts);
        model.addAttribute("products", fetchedProducts);
        return "view001";
    }
    
    @GetMapping("/dynamicsizes")
	@ResponseBody
	public List<Size>  getSizesByCategory(@RequestParam("categoryId")  List<Integer> categoryIds,Model model) {
        List<Size> sizes = productService.getSizesByCategory(categoryIds);
        return sizes;
    }

}
