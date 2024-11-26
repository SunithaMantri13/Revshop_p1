package com.project.revshop.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class BuyerController {	
	@GetMapping("/buyerDashboard")
	public String buyerPage() {
	  return "buyerDashboard";
	}
}

