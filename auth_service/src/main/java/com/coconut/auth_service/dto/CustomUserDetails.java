package com.coconut.auth_service.dto;

import com.coconut.global.constant.SignInType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

  private long userId;

  // email을 username으로 사용
  private String username;

  private String password;

  private SignInType signInType;

  private String nickname;

  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public String toString() {
    return "CustomUserDetails{" +
            "userId=" + userId +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", authorities=" + authorities +
            '}';
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
