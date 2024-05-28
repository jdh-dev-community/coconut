package com.coconut.auth_service.factory.oauth2.provider;

import com.coconut.auth_service.factory.oauth2.interfaces.OauthProvider;
import com.coconut.global.constant.SignInType;
import com.coconut.global.dto.UserCreateReqDto;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GoogleAuthProvider implements OauthProvider {
  public final SignInType PROVIDER_NAME = SignInType.GOOGLE;

  @Override
  public String getProviderName() {
    return PROVIDER_NAME.getType();
  }

  @Override
  public UserCreateReqDto parseOAuth2User(OAuth2User user) {


    String email = user.getAttribute("email");
    String nickname = UUID.randomUUID().toString();

    return UserCreateReqDto.of(PROVIDER_NAME, email, nickname);
  }
}
