package com.coconut.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)

@Schema(description = "로그인시에 auth서버와 user서버 간 통신을 위한 dto")
public class AuthUserDetails {
  private String provider;

  private String userId;

  private String email;

  private String password;

  public static AuthUserDetails of (String userId, String email, String password) {
    return new AuthUserDetails("coconut", userId, email, password);
  }

  public static AuthUserDetails of (String provider, String userId, String email, String password) {
    return new AuthUserDetails(provider, userId, email, null);
  }

  @Override
  public String toString() {
    return "AuthUserDetails{" +
            "userId=" + userId +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            '}';
  }
}
