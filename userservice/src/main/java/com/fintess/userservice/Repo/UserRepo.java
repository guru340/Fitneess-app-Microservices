package com.fintess.userservice.Repo;

import com.fintess.userservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    

    Boolean existsBykeyclockId(String userId);

    User findByEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email Format") String email);
}
//