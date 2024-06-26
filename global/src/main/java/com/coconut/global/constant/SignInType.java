package com.coconut.global.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum SignInType {
  EMAIL("email"),
  NAVER("naver"),

  GOOGLE("google");

  private final String type;

  SignInType(String type) { this.type = type.toLowerCase();}

  @JsonValue
  public String getType() { return this.type; }

  public static SignInType match(String type) {
    return Arrays.stream(SignInType.values())
            .filter((c) -> c.getType().equalsIgnoreCase(type))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("email, naver, google 중에서 선택해주세요. 입력: " + type));
  }

}
