package com.coconut.auth_service.dto;

import com.coconut.global.constant.SignInType;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@ToString
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IdPasswordSignInDto {

  @Schema(description = "유저Email", example = "aefawef@coconut.co.kr")
  private final String email;

  @Schema(description = "유저PW", example = "aafe@@sfewe12")
  private final String password;

  @Schema(description = "전화번호", example = "010-1111-1111")
  private final String mobile;

  private final SignInType signInType;

  @JsonCreator
  public static IdPasswordSignInDto of(String email, String password, String mobile) {
    return IdPasswordSignInDto.builder()
            .email(email)
            .password(password)
            .mobile(mobile)
            .signInType(SignInType.EMAIL)
            .build();
  }


}
