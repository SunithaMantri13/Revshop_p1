package com.project.revshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.revshop.entity.Order;
import com.project.revshop.entity.UserModel;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<Order> findByUserModelOrderByOrderDateDesc(UserModel userModel);
}
