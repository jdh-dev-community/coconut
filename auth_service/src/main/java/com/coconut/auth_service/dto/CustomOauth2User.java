package com.coconut.auth_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


@Getter
@ToString
@Builder
public class CustomOauth2User implements OAuth2User {

  private Map<String, Object> attributes;

  private Collection<? extends GrantedAuthority> authorities;

  private String name;

  private long userId;


  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }
}
