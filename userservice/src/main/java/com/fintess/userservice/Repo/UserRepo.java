package com.fintess.userservice.Repo;

import com.fintess.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);
}
