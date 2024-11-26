package com.project.revshop.controller;

import com.project.revshop.entity.UserModel;
import com.project.revshop.service.UserService;
import com.project.revshop.entity.SellerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/v1/register")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        UserModel user = new UserModel();
        SellerModel seller = new SellerModel();
        user.setSellermodel(seller);
        model.addAttribute("user", user);
        return "Register";
    }
    
//    @GetMapping("/buyerDashboard")
//    public String buyerPage() {
//        return "buyerDashboard";
//    }

    @PostMapping
    public String  processRegistration(
            @ModelAttribute("user") UserModel userModel,Model model) {
 	
        if ("seller".equals(userModel.getUserRole())) {
            SellerModel seller = userModel.getSellermodel();
            if (seller != null) {
                seller.setUsermodel(userModel);              
                UserModel savedUser = userService.saveUser(userModel);
                seller.setUsermodel(savedUser); 
                userService.saveSeller(seller);
            } else {
                model.addAttribute("error", "Seller details are missing");

                return "Register";
            }
        } else {
        	userModel.setWalletBalance(5500.0);
            userService.saveUser(userModel); 
        }
        return "redirect:/api/v1/login";
       
    }

}