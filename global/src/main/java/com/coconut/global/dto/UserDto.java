package com.coconut.global.dto;


import com.coconut.global.constant.SignInType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {

  @Schema(description = "유저 id", example = "1")
  private long userId;

  @Schema(description = "이메일", example = "awf@coconut.com")
  private String email;

  @Schema(description = "비밀번호", example = "feawfp3@3!2_awe")
  private String passowrd;

  @Schema(description = "닉네임", example = "따뜻한 키위")
  private String nickname;

  @Schema(description = "전화번호", example = "010-1111-1111")
  private String mobile;

  @Schema(description = "회원가입 타입", example = "Email")
  private SignInType signinType;

  @Schema(description = "관심분야", example = "010-1111-1111")
  private String interest;


}
