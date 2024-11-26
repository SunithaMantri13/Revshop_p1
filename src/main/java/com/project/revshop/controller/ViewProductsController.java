package com.project.revshop.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.revshop.entity.Category;
import com.project.revshop.entity.Product;
import com.project.revshop.service.ProductService;
import com.project.revshop.service.WishlistService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping
public class ViewProductsController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private WishlistService wishlistService;
		
	@GetMapping("/api/v1/buyer-dashboard")
	public String viewDashboard() {
		return "buyerDashboard";
	}

	@GetMapping("/api/v1/products")
	public String viewProductsPage(HttpSession session, Model model) {
		List<Product> products = productService.getAllProducts();

		List<Category> categories= productService.getAllCategories();
		
		model.addAttribute("categories",categories);
		
		Integer userId = (Integer) session.getAttribute("userId");
		List<Product> wishlistProducts = wishlistService.getWishlist(userId);
		model.addAttribute("products", products);
		model.addAttribute("wishlistProducts", wishlistProducts);
		return "view001";
	}
	
	@GetMapping("/api/v1/products/{id}")
	public String viewProductsById(@PathVariable("id") int productId, Model model) {
		Product products = productService.getProductById(productId);
        model.addAttribute("product", products);
//        double averageRating = productService.getRating(productId);
//        System.out.println(averageRating);
//        model.addAttribute("averageRating", averageRating);
//     	model.addAttribute("selectedProduct", products);  
        return "ProductDetails"; 
	}	
}
