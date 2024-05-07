package com.coconut.auth_service.factory.oauth2.provider;

import com.coconut.auth_service.factory.oauth2.interfaces.OauthProvider;
import com.coconut.global.dto.AuthUserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthProvider implements OauthProvider {
  public final String PROVIDER_NAME = "google";

  @Override
  public String getProviderName() {
    return PROVIDER_NAME;
  }

  @Override
  public AuthUserDetails parseOAuth2User(OAuth2User user) {
    String providerId = user.getAttribute("sub");
    String email = user.getAttribute("email");

    String userId = PROVIDER_NAME + "_" + providerId;

    return AuthUserDetails.of(PROVIDER_NAME, userId, email, null);
  }
}
