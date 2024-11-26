//package com.project.revshop.service;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.junit.jupiter.api.Assertions.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.revshop.entity.SellerModel;
//import com.project.revshop.entity.UserModel;
//import com.project.revshop.repository.SellerRepository;
//import com.project.revshop.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private SellerRepository sellerRepository;
//
//    @InjectMocks
//    private UserService userService;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testSaveUser() {
//        UserModel user = new UserModel();
//        when(userRepository.save(any(UserModel.class))).thenReturn(user);
//
//        UserModel savedUser = userService.saveUser(user);
//        assertEquals(user, savedUser);
//        verify(userRepository).save(any(UserModel.class));
//    }
//
//    @Test
//    public void testSaveSeller() {
//        SellerModel seller = new SellerModel();
//        when(sellerRepository.save(any(SellerModel.class))).thenReturn(seller);
//
//        SellerModel savedSeller = userService.saveSeller(seller);
//        assertEquals(seller, savedSeller);
//        verify(sellerRepository).save(any(SellerModel.class));
//    }
//
//    @Test
//    public void testValidateLogin() {
//        UserModel user = new UserModel();
//        when(userRepository.findByUserEmailAndUserPassword("test@example.com", "password")).thenReturn(user);
//
//        UserModel loggedInUser = userService.validateLogin("test@example.com", "password");
//        assertEquals(user, loggedInUser);
//        verify(userRepository).findByUserEmailAndUserPassword("test@example.com", "password");
//    }
//
//    @Test
//    public void testUpdatePassword() {
//        UserModel user = new UserModel();
//        user.setUserEmail("test@example.com");
//        user.setUserPassword("newPassword");
//
//        when(userRepository.findByUserEmail("test@example.com")).thenReturn(user);
//
//        userService.updatePassword("newPassword", "test@example.com");
//
//        assertEquals("newPassword", user.getUserPassword());
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    public void testUpdateUserProfile() {
//        UserModel user = new UserModel();
//        when(userRepository.save(any(UserModel.class))).thenReturn(user);
//
//        userService.updateUserProfile(user);
//
//        verify(userRepository).save(any(UserModel.class));
//    }
//}
