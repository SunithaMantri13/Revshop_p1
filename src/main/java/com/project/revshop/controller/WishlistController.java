package com.project.revshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.revshop.entity.Product;
import com.project.revshop.service.WishlistService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/v1/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;
    
    // @GetMapping
    // public String showWishlist(HttpSession session, Model model) {
    // 	Integer userId = (Integer) session.getAttribute("userId");
    // 	List<Product> products = wishlistService.getWishlist(userId);
    // 	model.addAttribute("products", products);
    // 	return "wishlist";
    // }

      @GetMapping
    public String showWishlist(HttpSession session, Model model) {
    	Integer userId = (Integer) session.getAttribute("userId");
    	List<Product> products = wishlistService.getWishlist(userId);
    	model.addAttribute("products", products);
    	return "wishlist";
    }

    @PostMapping
    public String addToWishlist(HttpSession session, @RequestParam Integer productId) {
    	Integer userId = (Integer) session.getAttribute("userId");
        wishlistService.addToWishlist(userId, productId);
        return "redirect:/api/v1/wishlist";
    }

    @DeleteMapping
    public String removeFromWishlist(HttpSession session, @RequestParam Integer productId) {
    	// Integer userId = (Integer) session.getAttribute("userId");

        Integer userId = (Integer) session.getAttribute("userId");
        
        wishlistService.removeFromWishlist(userId, productId);
        return "redirect:/api/v1/wishlist";
    }
}
