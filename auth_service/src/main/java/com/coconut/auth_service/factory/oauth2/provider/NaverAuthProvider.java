package com.coconut.auth_service.factory.oauth2.provider;

import com.coconut.auth_service.factory.oauth2.interfaces.OauthProvider;
import com.coconut.global.dto.AuthUserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NaverAuthProvider implements OauthProvider {
  private final String PROVIDER_NAME = "naver";
  private final String RESPONSE_KEY = "response"; // 네이버에서 받아온 유저정보의 response 객체에서 실제 데이터가 담겨있는 Key 값

  @Override
  public String getProviderName() {
    return PROVIDER_NAME;
  }

  @Override
  public AuthUserDetails parseOAuth2User(OAuth2User user) {
    Map<String, Object> response = user.getAttribute(RESPONSE_KEY);

    String providerId = (String) response.get("id");
    String email = (String) response.get("email");

    String userId = PROVIDER_NAME + "_" + providerId;

    return AuthUserDetails.of(PROVIDER_NAME, userId, email, null);
  }
}
