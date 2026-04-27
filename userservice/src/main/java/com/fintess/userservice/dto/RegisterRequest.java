package com.fintess.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email Format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=6,message = "Password must have atleast 8 Charcter")
    private String  password;
    private String firstname;
    private String lastname;
    private String keyclockId;


}
//

