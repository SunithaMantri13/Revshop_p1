

package com.project.revshop.service;

import com.project.revshop.entity.Cart;
import com.project.revshop.entity.OrderItem;
import com.project.revshop.entity.Product;
import com.project.revshop.entity.UserModel;
import com.project.revshop.repository.CartRepository;
import com.project.revshop.repository.ProductRepository;
import com.project.revshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    

      //  public void addToCart(Cart cart) {
      //  UserModel user = cart.getUser();

    
        public void addToCart(Cart cart) {
        UserModel user = cart.getUser();
        Product product = cart.getProduct();
        

            

        // Check if the product already exists in the user's cart
        Optional<Cart> existingCartItem = cartRepository.findByUserModelAndProduct(user, product);

        if (existingCartItem.isPresent()) {
            // If the product exists, update the quantity
            Cart updatedCart = existingCartItem.get();
            updatedCart.setQuantity(updatedCart.getQuantity() + cart.getQuantity());
            cartRepository.save(updatedCart);
        } else {
            // If the product doesn't exist in the cart, add it as a new item
            cartRepository.save(cart);
        }
    }


    public List<Cart> getCartItemsByuserModel(UserModel userModel) {
        return cartRepository.findByUserModel(userModel);
    }
  
    @Transactional
    public void clearCart(UserModel user) {
        cartRepository.deleteByUserModel(user);
    }

    public Cart findByUserAndProduct(int userId) {
      Cart cart = cartRepository.findByUserModelUserIdAndProductProductId(userId);
      return cart;
    }

    //
     public Cart findByUserAndProduct(int userId, int productId) {
      Cart cart = cartRepository.findByUserModelUserIdAndProductProductId(userId, productId);
      return cart;
    }

    public void deleteFromCart(Cart cart) {
      cartRepository.delete(cart);
    }

    public void updateCart(Cart cart) {
      cartRepository.save(cart);
    }
     
}

