package com.project.revshop.service;

import com.project.revshop.entity.Feedback;
import com.project.revshop.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitFeedback() {
        Feedback feedback = new Feedback();
        feedback.setId(1L); // assuming the feedback has an ID
        when(feedbackRepository.save(feedback)).thenReturn(feedback);

        Feedback submittedFeedback = feedbackService.submitFeedback(feedback);

        assertEquals(feedback, submittedFeedback);
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void testGetFeedbackForProduct() {
        Long productId = 1L;
        Feedback feedback1 = new Feedback();
        feedback1.setProductId(productId);

        Feedback feedback2 = new Feedback();
        feedback2.setProductId(productId);

        List<Feedback> feedbackList = Arrays.asList(feedback1, feedback2);
        when(feedbackRepository.findByProductId(productId)).thenReturn(feedbackList);

        List<Feedback> result = feedbackService.getFeedbackForProduct(productId);

        assertEquals(2, result.size());
        verify(feedbackRepository, times(1)).findByProductId(productId);
    }
}
