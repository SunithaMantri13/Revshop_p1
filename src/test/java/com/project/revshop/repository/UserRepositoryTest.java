//package com.project.revshop.repository;
//
//import com.project.revshop.entity.UserModel;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void testFindByUserEmailAndUserPassword() {
//        UserModel user = new UserModel("John", "john@example.com", "password", "user", "123 Main St", "1234567890");
//        userRepository.save(user);
//
//        UserModel foundUser = userRepository.findByUserEmailAndUserPassword("john@example.com", "password");
//        assertNotNull(foundUser);
//        assertEquals("john@example.com", foundUser.getUserEmail());
//    }
//
//    @Test
//    public void testFindByUserEmail() {
//        UserModel user = new UserModel("Jane", "jane@example.com", "password", "user", "456 Elm St", "0987654321");
//        userRepository.save(user);
//
//        UserModel foundUser = userRepository.findByUserEmail("jane@example.com");
//        assertNotNull(foundUser);
//        assertEquals("jane@example.com", foundUser.getUserEmail());
//    }
//}
