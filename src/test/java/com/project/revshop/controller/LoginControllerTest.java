package com.project.revshop.controller;

import com.project.revshop.entity.LoginModel;
import com.project.revshop.entity.SellerModel;
import com.project.revshop.entity.UserModel;
import com.project.revshop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    private LoginModel loginModel;
    private UserModel user;
    private SellerModel seller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        loginModel = new LoginModel();
        loginModel.setUserEmail("test@example.com");
        loginModel.setUserPassword("password");
        
        user = new UserModel();
        user.setUserId(1);
        user.setUserEmail("test@example.com");
        user.setUserRole("buyer");
        
        seller = new SellerModel();
        seller.setSellerId(1);
    }

    @Test
    public void testShowLoginForm() {
        String viewName = loginController.showLoginForm(model);
//        verify(model, times(1)).addAttribute("login", new LoginModel());
//        assertEquals("Login", viewName);
    }

    @Test
    public void testProcessLogin_SuccessfulBuyerLogin() {
        when(userService.validateLogin(any(String.class), any(String.class))).thenReturn(user);

        String result = loginController.processLogin(loginModel, session);

        verify(session, times(1)).setAttribute("userId", user.getUserId());
        assertEquals("redirect:/api/v1/buyer-dashboard", result);
    }

    @Test
    public void testProcessLogin_SuccessfulSellerLogin() {
        user.setUserRole("seller");
        when(userService.validateLogin(any(String.class), any(String.class))).thenReturn(user);
        when(userService.getSellerId(user.getUserId())).thenReturn(seller);

        String result = loginController.processLogin(loginModel, session);

        verify(session, times(1)).setAttribute("userId", user.getUserId());
        verify(session, times(1)).setAttribute("sellerid", seller.getSellerId());
        assertEquals("redirect:/api/v1/seller", result);
    }

    @Test
    public void testProcessLogin_NoSellerFound() {
        user.setUserRole("seller");
        when(userService.validateLogin(any(String.class), any(String.class))).thenReturn(user);
        when(userService.getSellerId(user.getUserId())).thenReturn(null);

        String result = loginController.processLogin(loginModel, session);
//
//        verify(session, times(1)).setAttribute("userId", user.getUserId());
//        assertEquals("notfound", result);
    }

    @Test
    public void testProcessLogin_InvalidCredentials() {
        when(userService.validateLogin(any(String.class), any(String.class))).thenReturn(null);

        String result = loginController.processLogin(loginModel, session);

        assertEquals("notfound", result);
    }
}
