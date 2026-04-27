package com.fitness.gatewayservices.user;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String email;
    private String keyclockId;
    private String password;
    private String lastname;

    private LocalDateTime createdAT;
    private LocalDateTime updatedAt;
}
//