package com.fintess.userservice;

import com.fintess.userservice.Repo.UserRepo;
import com.fintess.userservice.Services.UserService;
import com.fintess.userservice.dto.RegisterRequest;
import com.fintess.userservice.dto.UserResponse;
import com.fintess.userservice.model.User;
import com.fintess.userservice.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureDataSourceInitialization;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    
    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void cleanDB(){
        userRepo.deleteAll();
    }

    @Test
    void shouldCreatedUser() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setFirstname("Mayank");
        request.setLastname("Sangwani");
        request.setEmail("u@gmail.com");
        request.setPassword("12345");

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setFirstName("Mayank");
        response.setEmail("u@gmail.com");

        when(userService.register(ArgumentMatchers.any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mayank"))
                .andExpect(jsonPath("$.email").value("u@gmail.com"));
    }
}

