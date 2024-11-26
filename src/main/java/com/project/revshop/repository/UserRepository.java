package com.project.revshop.repository;

import com.project.revshop.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
	 UserModel findByUserEmailAndUserPassword(String userEmail, String userPassword);	
	 UserModel findByUserEmail(String userEmail);
	UserModel findByUserId(int userId);
}
