package com.fintess.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;

    @Column(unique = true)
    private String email;

    private String keyclockId;

    @Column(nullable = false)
    private String password;
    private String lastname;

    @Enumerated(EnumType.STRING)
    private  UserRole role=UserRole.USER;

    @CreationTimestamp
    private LocalDateTime createdAT;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
//