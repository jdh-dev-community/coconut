package com.coconut.auth_service.service;

import com.coconut.global.dto.AuthUserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Oauth2ProviderService {

  public void getOauth2AuthUser(String provider, OAuth2User user) {

    if (provider.equals("naver")) {
      handleNaverUser(user);
    } else if (provider.equals("google")) {
      handleGoogleUser(user);
    }

  }

  private void handleNaverUser(OAuth2User user) {}

  private void handleGoogleUser(OAuth2User user) {}

}
