package com.project.revshop.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.revshop.entity.Product;
import com.project.revshop.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/v1/seller")
public class SellerController {

	@Autowired
	private ProductService productService;
	
	@GetMapping
	public String showSeller() {
		return "sellerDashboard";
	}
	
	@GetMapping("/products")
	public String showProducts(HttpSession session, Model model) {
		int sellerId = (int) session.getAttribute("sellerid");
		List<Product> products = productService.getProductsBySeller(sellerId);
		model.addAttribute("products", products);
		return "sellerProducts";
	}
}
