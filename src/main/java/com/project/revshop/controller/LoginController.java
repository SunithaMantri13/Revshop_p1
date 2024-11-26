package com.project.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.revshop.entity.LoginModel;
import com.project.revshop.entity.SellerModel;
import com.project.revshop.entity.UserModel;
import com.project.revshop.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/v1/login")
public class LoginController {

	@Autowired
	private UserService userservice;

	@GetMapping
	public String showLoginForm(Model model) {
		model.addAttribute("login", new LoginModel());
		return "Login";

	}

	@PostMapping
	public String processLogin(@ModelAttribute("login") LoginModel loginModel, HttpSession session) {
		UserModel user = userservice.validateLogin(loginModel.getUserEmail(), loginModel.getUserPassword());

		if (user != null) {
			Integer fetcheduserId = (user.getUserId());
			System.out.println("fetcheduser ID: " + fetcheduserId);
			session.setAttribute("userId", fetcheduserId);
			System.out.println("Login successful! Welcome, " + user.getUserEmail());

			if ("buyer".equals(user.getUserRole())) {
				return "redirect:/api/v1/buyer-dashboard";	
			} else {
				SellerModel seller = userservice.getSellerId(fetcheduserId);
				if (seller != null) {
					int sellerId = seller.getSellerId();
					session.setAttribute("sellerid", sellerId);

					System.out.println("Seller ID: " + sellerId);
					return "redirect:/api/v1/seller";

				} else {
					System.out.println("No seller found for user ID: " + fetcheduserId);
					return "notfound";
				}
			}
		} else {
			return "notfound";
		}
	}

}
