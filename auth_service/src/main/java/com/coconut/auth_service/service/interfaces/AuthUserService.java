package com.coconut.auth_service.service.interfaces;

import com.coconut.global.dto.AuthUserDetails;

public interface AuthUserService {
  AuthUserDetails getUserFromUseServer(String username);
}
