package com.coconut.user_service.domain.user.dto;


import com.coconut.user_service.domain.user.constant.SignInType;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;

@Slf4j
@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreateDto {

  @Schema(description = "유저Email", example = "aefawef@coconut.co.kr")
  private final String email;

  @Schema(description = "유저PW", example = "aafe@@sfewe12")
  private final String password;

  @Schema(description = "전화번호", example = "010-1111-1111")
  private final String mobile;

  @Schema(description = "가입유형 ( email, google ... )", example = "010-1111-1111")
  @NotNull(message = "회원가입 타입은 필수 입력 값입니다.")
  private final SignInType signinType;

  @Schema(description = "관심사", example = "backend")
  private final String interest;

  @Schema(description = "유저 닉네임", example = "backend")
  private final String nickname;


  @JsonCreator
  public static UserCreateDto of(String email, String password, String mobile, String signinType, String interest) {
    return UserCreateDto.builder()
            .email(email)
            .password(password)
            .mobile(mobile)
            .signinType(SignInType.match(signinType))
            .interest(interest)
            .nickname("will be random string")
            .build();
  }

  @Override
  public String toString() {
    return "UserCreateDto{" +
            "email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", mobile='" + mobile + '\'' +
            ", signinType=" + signinType +
            ", interest='" + interest + '\'' +
            '}';
  }
}
