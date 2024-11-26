package com.project.revshop.service;

import com.project.revshop.entity.UserModel;
import com.project.revshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {

    @Autowired
    private UserRepository userRepository;
    @Transactional
    public boolean debit(UserModel user, double amount) {
        if (user.getWalletBalance() >= amount) {
            user.setWalletBalance(user.getWalletBalance() - amount);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
