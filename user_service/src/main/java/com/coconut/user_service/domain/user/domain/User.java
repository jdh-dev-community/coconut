package com.coconut.user_service.domain.user.domain;

import com.coconut.global.domain.BaseEntity;
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
  @Column(name = "user_id")
  private long userId;

  @Schema(description = "유저의 id", example = "apeofj@coconut.com")
  @Column(name = "email")
  private String email;

  @Schema(description = "유저의 pw", example = "a@@efae123")
  @Column(name = "password")
  private String password;

  @Schema(description = "유저의 닉네임", example = "따뜻한키위")
  @Column(name = "nickname")
  private String nickname;

  @Schema(description = "유저의 전화번호", example = "010-1111-1111")
  @Column(name = "mobile")
  private String mobile;

  @Schema(description = "회원가입 방법 (이메일, 구글 ... )", example = "email")
  @Column(name = "signin_type")
  private String signinType;

  @Schema(description = "유저의 관심분야", example = "backend")
  @Column(name = "interest")
  private String interest;

  @Builder
  public User(long userId, String email, String password, String nickname, String mobile, String signinType, String interest) {
    this.userId = userId;
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.mobile = mobile;
    this.signinType = signinType;
    this.interest = interest;
  }

  @Override
  public String toString() {
    return "User{" +
            "user_id=" + userId +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", nickname='" + nickname + '\'' +
            ", mobile='" + mobile + '\'' +
            ", signinType='" + signinType + '\'' +
            ", interest='" + interest + '\'' +
            '}';
  }
}
