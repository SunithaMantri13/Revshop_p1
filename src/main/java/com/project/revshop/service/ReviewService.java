package com.project.revshop.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.revshop.entity.Review;
import com.project.revshop.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	public void addReview(Review review) {
		reviewRepository.save(review);
	} 
	 
	
}
