package com.coconut.user_service.domain.user.repository;

import com.coconut.user_service.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
