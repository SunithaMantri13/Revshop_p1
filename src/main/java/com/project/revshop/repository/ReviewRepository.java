package com.project.revshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.revshop.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{
	
}
