package com.project.revshop.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.revshop.entity.Feedback;
import com.project.revshop.repository.FeedbackRepository;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback submitFeedback(Feedback feedback) {
//        feedback.setSubmittedAt(new Date());
        return feedbackRepository.save(feedback);  // Use save() for a single entity
    }

    public List<Feedback> getFeedbackForProduct(Long productId) {
        return feedbackRepository.findByProductId(productId);
    }
}

