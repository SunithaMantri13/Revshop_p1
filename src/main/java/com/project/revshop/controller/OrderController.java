package com.project.revshop.controller;

import com.project.revshop.entity.*;

import com.project.revshop.service.*;
import com.razorpay.Order;  // Import Razorpay's Order
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private EmailService emailService;

    private RazorpayClient razorpay;

    public OrderController() {
        try {
            this.razorpay = new RazorpayClient("rzp_test_0D5sE2a7VoD1n7", "NChlSEoFcXSdW9dYD1zHcWgS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public String checkout(Model model, HttpSession session) {
        int userid = (Integer) session.getAttribute("userId");
        UserModel userModel = userService.getUserId(userid);
        List<Cart> cartItems = cartService.getCartItemsByuserModel(userModel);
        double total = 0;
        for (Cart ct : cartItems) {
        	total += (ct.getQuantity() * (ct.getProduct().getPrice()-((ct.getProduct().getPrice()*ct.getProduct().getDiscountPrice())/100)));        }
        model.addAttribute("wallet", userModel.getWalletBalance());
        model.addAttribute("total", total);
        model.addAttribute("cartItems", cartItems);
        return "checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String shippingAddress,
                             @RequestParam String billingAddress,
                             HttpSession session, Model model) {
        int userid = (Integer) session.getAttribute("userId");
        UserModel user = userService.getUserId(userid);
        if (user == null) {
            return "redirect:/api/v1/login";
        }

        List<Cart> cartItems = cartService.getCartItemsByuserModel(user);
        if (cartItems.isEmpty()) {
            model.addAttribute("error", "Your cart is empty");
            return "checkout";
        }

        // Create the order
        com.project.revshop.entity.Order order = orderService.createOrder(user, cartItems, shippingAddress, billingAddress);
        cartService.clearCart(user);
        try {
            emailService.sendOTPMessage(user.getUserEmail(), "Placed an order", "Your order has been placed");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        double orderAmount = order.getTotalAmount();

        // // Create Razorpay order
        // String razorpayOrderId = createRazorpayOrder(orderAmount);
        // model.addAttribute("razorpayOrderId", razorpayOrderId);
    

         // Create Razorpay order
        String razorpayOrderId = createRazorpayOrder(orderAmount);
        model.addAttribute("razorpayOrderId", razorpayOrderId);
        model.addAttribute("orderAmount", orderAmount );  // Amount in paise
        
        model.addAttribute("userEmail", user.getUserEmail());
        model.addAttribute("userName", user.getUserName());
        // model.addAttribute("userContact", user.getUserContact());

        // model.addAttribute("orderItems", cartItems);
        // model.addAttribute("order", order);
        // System.out.println("Payment page ");
        // System.out.println(razorpayOrderId);

        model.addAttribute("orderItems", cartItems);
        model.addAttribute("order", order);
        System.out.println("Payment page ");
        //checking the razorpayOrderId
        System.out.println(razorpayOrderId);
        
        System.out.println(orderAmount);
        
        return "payment";
    }

    private String createRazorpayOrder(double amount) {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int) (amount * 100)); // Amount in paise
            System.out.println("ampount=" + amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_123456");

            Order razorpayOrder = razorpay.orders.create(orderRequest);
            return razorpayOrder.get("id");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    //thankyou
    
    @GetMapping("/thankyou")
    public String thankYouPage() {
        return "thankyou";
    }
    
   


    @PostMapping("/payment/success")
    public String handlePaymentSuccess(@RequestParam("razorpay_payment_id") String paymentId,
                                       @RequestParam("razorpay_order_id") String orderId,
                                       @RequestParam("razorpay_signature") String signature,
                                       HttpSession session, Model model) {

        int userid = (Integer) session.getAttribute("userId");
        UserModel user = userService.getUserId(userid);
        
        // Convert String orderId to Integer
        Integer numericOrderId;
        try {
            numericOrderId = Integer.valueOf(orderId);
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid order ID");
            return "checkout"; // Handle invalid order ID gracefully
        }

        // Use the converted order ID
        com.project.revshop.entity.Order order = orderService.getOrderById(numericOrderId);
        
        // Verify payment and process order
        boolean paymentSuccess = verifyPaymentSignature(paymentId, orderId, signature);
        if (paymentSuccess) {
            orderService.updateOrderStatus(order, com.project.revshop.entity.Order.OrderStatus.Shipped);
            model.addAttribute("order", order);
            return "orderConfirmation";
        } else {
            model.addAttribute("error", "Payment verification failed");
            return "checkout";
        }
    }


    private boolean verifyPaymentSignature(String paymentId, String orderId, String signature) {
        // Implement Razorpay signature verification logic here
        return true;  // Assuming success for this example
    }




    @GetMapping("/history")
    public String orderHistory(HttpSession session, Model model) {
        int userid = (Integer) session.getAttribute("userId");
        UserModel user = userService.getUserId(userid);
        if (user == null) {
            return "redirect:/api/v1/login";
        }

        List<com.project.revshop.entity.Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        return "orderHistory";
    }
    @GetMapping("/orderHistory")
    public String orderHistoryBySeller(HttpSession session, Model model) {
        int sellerid = (Integer) session.getAttribute("sellerid");
        SellerModel sellerModel = userService.getSellerId(sellerid);
        if (sellerModel == null) {
            return "redirect:/api/v1/login";
        }
        List<com.project.revshop.entity.Order> orders = orderService.getOrdersForSeller();
        model.addAttribute("orders", orders);
        return "orderHistoryForSeller";
    }
    @GetMapping("/details")
    public String orderDetails(@RequestParam("orderId") int orderId, Model model) {
        com.project.revshop.entity.Order order = orderService.getOrderById(orderId);
        List<OrderItem> orderItems = orderService.getOrderItemsByOrder(order);
        model.addAttribute("orderItems", orderItems);
        return "orderDetails";
    }

}
