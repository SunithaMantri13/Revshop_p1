package com.project.revshop.controller;

import com.project.revshop.entity.UserModel;
import com.project.revshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewProfile(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            return "redirect:/api/v1/login";
        }
        
        UserModel user = userService.getUserId(userId);  
        model.addAttribute("user", user);  
        return "profile"; 
    }
    
    @GetMapping("/edit")
    public String editProfile(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/api/v1/login";
        }

        UserModel user = userService.getUserId(userId);
        model.addAttribute("user", user);
        return "editProfile";
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute("user") UserModel updatedUser, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/api/v1/login";
        }

        // Fetch the existing user from the database
        UserModel existingUser = userService.getUserId(userId);
        
        if (existingUser != null) {
            // Update only the fields that were submitted in the form
            existingUser.setUserName(updatedUser.getUserName());
            existingUser.setUserMobileNumber(updatedUser.getUserMobileNumber());
            existingUser.setUserPassword(updatedUser.getUserPassword());
            // Retain other fields like userRole, userPassword, etc.
            
            // Save the updated user back to the database
            userService.updateUserProfile(existingUser);
        }

        return "redirect:/api/v1/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Invalidate the session to log the user out
        return "redirect:/api/v1/login";  // Redirect to login after logout
    }


}

