package com.coconut.auth_service.service.interfaces;

import com.coconut.global.dto.AuthUserDetails;

public interface UserServerService {
  AuthUserDetails findUserByEmail(String email);
}
