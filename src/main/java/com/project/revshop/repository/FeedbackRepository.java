package com.project.revshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.revshop.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProductId(Long productId);
}