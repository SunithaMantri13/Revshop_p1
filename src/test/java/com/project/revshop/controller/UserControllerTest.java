package com.project.revshop.controller;

import com.project.revshop.entity.UserModel;
import com.project.revshop.entity.SellerModel;
import com.project.revshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowRegistrationForm() {
        String viewName = userController.showRegistrationForm(model);
        verify(model, times(1)).addAttribute(eq("user"), any(UserModel.class));
        assertEquals("Register", viewName);
    }

    @Test
    public void testProcessRegistration_Seller() {
        UserModel user = new UserModel();
        SellerModel seller = new SellerModel();
        user.setUserRole("seller");
        user.setSellermodel(seller);

        when(userService.saveUser(any(UserModel.class))).thenReturn(user);
        when(userService.saveSeller(any(SellerModel.class))).thenReturn(seller);

        String result = userController.processRegistration(user, model);

        assertEquals("redirect:/api/v1/login", result);
        verify(userService, times(1)).saveUser(user);
        verify(userService, times(1)).saveSeller(seller);
    }

    @Test
    public void testProcessRegistration_NonSeller() {
        UserModel user = new UserModel();
        user.setUserRole("user");

        when(userService.saveUser(any(UserModel.class))).thenReturn(user);

        String result = userController.processRegistration(user, model);

        assertEquals("redirect:/api/v1/login", result);
        verify(userService, times(1)).saveUser(user);
        verify(userService, times(0)).saveSeller(any(SellerModel.class));
    }

    @Test
    public void testProcessRegistration_Seller_MissingDetails() {
        UserModel user = new UserModel();
        user.setUserRole("seller");
        user.setSellermodel(null); // Missing seller details

        String result = userController.processRegistration(user, model);

        assertEquals("Register", result);
        verify(model, times(1)).addAttribute(eq("error"), eq("Seller details are missing"));
        verify(userService, times(0)).saveUser(any(UserModel.class));
        verify(userService, times(0)).saveSeller(any(SellerModel.class));
    }
}
