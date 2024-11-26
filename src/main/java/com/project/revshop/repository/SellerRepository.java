package com.project.revshop.repository;

import com.project.revshop.entity.SellerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<SellerModel, Integer> {
	SellerModel findByUsermodelUserId(Integer userId);
	
}
