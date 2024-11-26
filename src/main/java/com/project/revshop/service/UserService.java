package com.project.revshop.service;

import com.project.revshop.entity.SellerModel;
import com.project.revshop.entity.UserModel;
import com.project.revshop.repository.SellerRepository;
import com.project.revshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;

    public UserModel saveUser(UserModel user) {
        return userRepository.save(user);
    }
    
    public SellerModel saveSeller(SellerModel sellermodel) {    	
    	return sellerRepository.save(sellermodel);   	
    }
    
    public UserModel validateLogin(String userEmail , String userPassword) {    	
    	return userRepository.findByUserEmailAndUserPassword(userEmail, userPassword);	    	
    }
    
    
    public SellerModel getSellerId(Integer userID) {
		return sellerRepository.findByUsermodelUserId(userID);
	}

	public void updatePassword(String password, String key) {
		UserModel userModel = userRepository.findByUserEmail(key);
		userModel.setUserPassword(password);
		userRepository.save(userModel);
	}

	public UserModel getUserId(int userId) {
		// TODO Auto-generated method stub
		return userRepository.findByUserId(userId);
	}
	
	public void updateUserProfile(UserModel user) {
		userRepository.save(user);
	}

	public Object findByEmail(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object isPasswordCorrect(UserModel userModel, String string) {
		// TODO Auto-generated method stub
		return null;
	}
}