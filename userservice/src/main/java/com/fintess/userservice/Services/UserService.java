package com.fintess.userservice.Services;

import com.fintess.userservice.Repo.UserRepo;
import com.fintess.userservice.dto.RegisterRequest;
import com.fintess.userservice.dto.UserResponse;
import com.fintess.userservice.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    public UserResponse register(RegisterRequest request){

        if(userRepo.existsByEmail( request.getEmail())){
            User existinguser=userRepo.findByEmail(request.getEmail());
            UserResponse userResponse=new UserResponse();
            userResponse.setId(existinguser.getId());
            userResponse.setRole(existinguser.getRole());
            userResponse.setFirstName(existinguser.getFirstName());
            userResponse.setLastname(existinguser.getLastname());
            userResponse.setEmail(existinguser.getEmail());
            userResponse.setPassword(existinguser.getPassword());
            userResponse.setCreatedAT(existinguser.getCreatedAT());
            userResponse.setUpdatedAt(existinguser.getUpdatedAt());

            return userResponse;
        }
        User user=new User();
        user.setFirstName(request.getFirstname());
        user.setEmail(request.getEmail());
        user.setKeyclockId(request.getKeyclockId());
        user.setPassword(request.getPassword());
        user.setLastname(request.getLastname());
        user.setLastname(request.getLastname());

        User saveduer=userRepo.save(user);
        UserResponse userResponse=new UserResponse();
        userResponse.setId(saveduer.getId());
        userResponse.setRole(saveduer.getRole());
        userResponse.setFirstName(saveduer.getFirstName());
        userResponse.setLastname(saveduer.getLastname());
        userResponse.setEmail(saveduer.getEmail());
        userResponse.setPassword(saveduer.getPassword());
        userResponse.setCreatedAT(saveduer.getCreatedAT());
        userResponse.setUpdatedAt(saveduer.getUpdatedAt());

        return userResponse;
    }

    public  UserResponse getUserProfile(String userId) {
            User user=userRepo.findById(Long.valueOf(userId)).orElseThrow(()->new RuntimeException("User not Found"));

        User saveduer=userRepo.save(user);
        UserResponse userResponse=new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setRole(user.getRole());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastname(user.getLastname());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setCreatedAT(user.getCreatedAT());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    public  Boolean existdBYId(String userId) {
        log.info("Calling user service",userId);
        return userRepo.existsBykeyclockId(userId);
    }
}
//