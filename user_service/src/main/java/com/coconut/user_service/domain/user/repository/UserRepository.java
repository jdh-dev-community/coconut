package com.coconut.user_service.domain.user.repository;

import com.coconut.user_service.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailAndSignInType(String email, String SignInType);
}
