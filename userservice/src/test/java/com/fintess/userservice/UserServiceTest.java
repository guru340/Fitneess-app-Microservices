package com.fintess.userservice;

import com.fintess.userservice.Repo.UserRepo;
import com.fintess.userservice.Services.UserService;
import com.fintess.userservice.dto.UserResponse;
import com.fintess.userservice.model.User;
import com.fintess.userservice.model.UserRole;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Test
    void shouldAddStudentSucessFully(){
        User user=new User();
          user.setFirstName("Mayk");
        user.setLastname("Sangni");
        user.setEmail("u"+ System.currentTimeMillis()+"@gmail.com");
        user.setPassword("12345");
        user.setKeyclockId("kc123");
        user.setRole(UserRole.USER);

        User savedUser = new User();

        savedUser.setId(1L);
        savedUser.setFirstName("Mayk");
        savedUser.setLastname("Sangni");
        savedUser.setEmail("u"+ System.currentTimeMillis()+"@gmail.com");
        savedUser.setPassword("12345");
        savedUser.setKeyclockId("kc123");
        savedUser.setRole(UserRole.USER);

        userRepo.save(user);


        UserResponse result = userService.getUserProfile("2");

        assertNotNull(result);
        assertEquals(1L,result.getId());
        assertEquals("Mayk", result.getFirstName());
        assertEquals("u"+System.currentTimeMillis()+"@gmail.com", result.getEmail());
        assertEquals(UserRole.USER, result.getRole());

        verify(userRepo, times(1)).save(user);

    }
}
