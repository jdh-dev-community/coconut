package com.coconut.user_service.domain.user.domain;

import com.coconut.global.constant.SignInType;
import com.coconut.jpa_utils.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @Schema(description = "uid", example = "1")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "user_id", nullable = false)
  private long userId;

  @Schema(description = "유저의 id", example = "apeofj@coconut.com")
  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Schema(description = "유저의 pw", example = "a@@efae123")
  @Column(name = "password")
  private String password;

  @Schema(description = "유저의 닉네임", example = "따뜻한키위")
  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Schema(description = "유저의 전화번호", example = "010-1111-1111")
  @Column(name = "mobile")
  private String mobile;

  @Schema(description = "회원가입 방법 (이메일, 구글 ... )", example = "email")
  @Column(name = "signin_type", nullable = false)
  private String signInType;

  @Schema(description = "유저의 관심분야", example = "backend")
  @Column(name = "interest")
  private String interest;

  @Builder
  public User(long userId, String email, String password, String nickname, String mobile, SignInType signInType, String interest) {
    this.userId = userId;
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.mobile = mobile;
    this.signInType = signInType.getType();
    this.interest = interest;
  }

  @Override
  public String toString() {
    return "User{" +
            "userId=" + userId +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", nickname='" + nickname + '\'' +
            ", mobile='" + mobile + '\'' +
            ", singInType='" + signInType + '\'' +
            ", interest='" + interest + '\'' +
            '}';
  }
}
