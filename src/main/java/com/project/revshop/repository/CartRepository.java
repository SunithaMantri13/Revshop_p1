package com.project.revshop.repository;

import com.project.revshop.entity.Cart;
import com.project.revshop.entity.Product;
import com.project.revshop.entity.UserModel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	List<Cart> findByUserModel(UserModel userModel);
	Optional<Cart> findByUserModelAndProduct(UserModel user, Product product);
  void deleteByUserModel(UserModel user);
	Cart findByUserModelUserIdAndProductProductId(Integer userId, Integer productId);
}
