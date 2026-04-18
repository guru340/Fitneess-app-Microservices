package com.fintess.userservice.dto;

import com.fintess.userservice.model.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String email;
    private String password;
    private String lastname;
    private UserRole role=UserRole.USER;
    private LocalDateTime createdAT;
    private LocalDateTime updatedAt;
}
