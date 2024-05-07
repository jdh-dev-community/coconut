package com.coconut.auth_service.service;

import com.coconut.auth_service.factory.oauth2.factory.Oauth2ProviderFactory;
import com.coconut.auth_service.factory.oauth2.interfaces.OauthProvider;
import com.coconut.auth_service.service.interfaces.UserServerService;
import com.coconut.global.dto.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

  private final UserServerService userServerService;

  private final Oauth2ProviderFactory oauth2ProviderFactory;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    String resourceProvider = userRequest.getClientRegistration().getRegistrationId();
    OAuth2User oAuth2User = super.loadUser(userRequest);

    OauthProvider provider = oauth2ProviderFactory.getOauth2AuthUserProvider(resourceProvider);
    AuthUserDetails authUserDetails = provider.parseOAuth2User(oAuth2User);
    upsertUser(authUserDetails);


    return oAuth2User;
  }

  private void upsertUser(AuthUserDetails details) {
    String email = details.getEmail();
    AuthUserDetails user = userServerService.findUserByEmail(email);
  }

}
