package com.coconut.global.dto;

import com.coconut.global.constant.SignInType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

// 회원 생성을 위한 dto

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)

@Schema(description = "로그인시에 auth서버와 user서버 간 통신을 위한 dto")
public class UserCreateReqDto {

  @Schema(description = "회원가입 타입 (email, google, naver .. )", example = "email")
  @NotNull
  private SignInType signinType;

  @Schema(description = "회원가입 이메일", example = "awefawe@google.com")
  @NotBlank
  private String email;

  @Schema(description = "회원가입 비밀번호 (id/pw 회원가입에서만 사용)", example = "Qaw252!")
  private String password;

  @Schema(description = "회원 전화번호", example = "010-2222-2124")
  private String mobile;

  @Schema(description = "닉네임", example = "goatuser")
  private String nickname;

  public static UserCreateReqDto of(SignInType signinType, String email, String password, String mobile, String nickname) {
    return new UserCreateReqDto(signinType, email, password, mobile, nickname);
  }

  public static UserCreateReqDto of(SignInType signinType, String email, String password) {
    return new UserCreateReqDto(signinType, email, password, null, null);
  }

}
