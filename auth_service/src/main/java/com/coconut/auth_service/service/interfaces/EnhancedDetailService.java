package com.coconut.auth_service.service.interfaces;

import com.coconut.auth_service.dto.IdPasswordSignInDto;
import com.coconut.global.constant.SignInType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface EnhancedDetailService extends UserDetailsService {
  UserDetails saveUser(IdPasswordSignInDto dto);
  UserDetails loadUserByEmailAndSignInType(String email, SignInType signInType);
}
