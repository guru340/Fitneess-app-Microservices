package com.fintess.userservice.Controller;

import com.fintess.userservice.Services.UserService;
import com.fintess.userservice.dto.RegisterRequest;
import com.fintess.userservice.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid  @RequestBody RegisterRequest requset){
        return ResponseEntity.ok(userService.register(requset));
    }

    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> getUserProfilevalidate(@PathVariable String userId){
        return ResponseEntity.ok(userService.existdBYId(userId));
    }
}
