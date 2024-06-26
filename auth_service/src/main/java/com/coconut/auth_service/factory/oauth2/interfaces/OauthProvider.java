package com.coconut.auth_service.factory.oauth2.interfaces;

import com.coconut.global.dto.UserCreateReqDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OauthProvider {

  String getProviderName();
  UserCreateReqDto parseOAuth2User(OAuth2User user);
}
