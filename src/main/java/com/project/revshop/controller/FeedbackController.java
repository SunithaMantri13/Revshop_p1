package com.project.revshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.revshop.entity.Feedback;
import com.project.revshop.service.FeedbackService;

@Controller
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping
    public String showFeedbackForm( Model model) {
//        model.addAttribute("productId", productId);
        model.addAttribute("feedback", new Feedback());
        return "Feedback";
    }

    @PostMapping
    public String submitFeedback(@ModelAttribute Feedback feedback, Model model) {
        feedbackService.submitFeedback(feedback);
        model.addAttribute("message", "Thank you for your feedback!");
        return "feedback_success";
    }
}

