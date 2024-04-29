package com.coconut.user_service.domain.user.dto;

import com.coconut.user_service.domain.user.constant.SignInType;
import com.coconut.user_service.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {


  @Schema(description = "유저 id", example = "1")
  private long userId;

  @Schema(description = "이메일", example = "awf@coconut.com")
  private String email;

  @Schema(description = "닉네임", example = "따뜻한 키위")
  private String nickname;

  @Schema(description = "전화번호", example = "010-1111-1111")
  private String mobile;

  @Schema(description = "회원가입 타입", example = "Email")
  private SignInType signinType;

  @Schema(description = "관심분야", example = "010-1111-1111")
  private String interest;

  public static UserDto from (User user) {
    return UserDto.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .mobile(user.getMobile())
            .signinType(SignInType.match(user.getSigninType()))
            .interest(user.getInterest())
            .build();
  }


  @Override
  public String toString() {
    return "UserDto{" +
            "userId=" + userId +
            ", email='" + email + '\'' +
            ", nickname='" + nickname + '\'' +
            ", mobile='" + mobile + '\'' +
            ", signinType=" + signinType +
            ", interest='" + interest + '\'' +
            '}';
  }
}
