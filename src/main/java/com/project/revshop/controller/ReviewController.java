package com.project.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.revshop.entity.Product;
import com.project.revshop.entity.Review;
import com.project.revshop.service.ProductService;
import com.project.revshop.service.ReviewService;

@Controller
@RequestMapping("/api/v1/reviews")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/{id}")
	public String displayReviewForm(Model model, @PathVariable int id) {
		model.addAttribute("review", new Review());
		model.addAttribute("productId", id);
		return "showReview";
	}
	
	@PostMapping("/{id}")
	public String addReview(@PathVariable int id, @ModelAttribute Review review) {
		System.out.println(review.getRating() + " " + review.getReviewContent());
		Product product = productService.findById(id);
		if(product != null) {
			review.setProduct(product);
			reviewService.addReview(review);
			return "redirect:/api/v1/orders/history";			
		}
		return "notfound";
	}
	
}
