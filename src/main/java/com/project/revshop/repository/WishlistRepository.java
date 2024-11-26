package com.project.revshop.repository;

import com.project.revshop.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserUserId(Integer userId);
   // Wishlist findByUserUserIdAndProductProductId(Integer userId, Integer productId);

      Wishlist findByUserUserIdAndProductProductId(Integer userId, Integer productId);

}
