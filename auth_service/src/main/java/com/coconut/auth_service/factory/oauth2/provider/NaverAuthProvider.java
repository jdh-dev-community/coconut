package com.coconut.auth_service.factory.oauth2.provider;

import com.coconut.auth_service.factory.oauth2.interfaces.OauthProvider;
import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class NaverAuthProvider implements OauthProvider {
  private final SignInType PROVIDER_NAME = SignInType.NAVER;
  private final String RESPONSE_KEY = "response"; // 네이버에서 받아온 유저정보의 response 객체에서 실제 데이터가 담겨있는 Key 값

  @Override
  public String getProviderName() {
    return PROVIDER_NAME.getType();
  }

  @Override
  public UserCreateReqDto parseOAuth2User(OAuth2User user) {
    Map<String, Object> response = user.getAttribute(RESPONSE_KEY);

    String email = (String) response.get("email");
    String nickname = UUID.randomUUID().toString();

    return UserCreateReqDto.of(PROVIDER_NAME, email, nickname);
  }
}
