package com.coconut.auth_service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtCreateDto {
  private long userId;

  public static JwtCreateDto of(long userId) {
    return new JwtCreateDto(userId);
  }

}
