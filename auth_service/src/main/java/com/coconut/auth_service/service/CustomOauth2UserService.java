package com.coconut.auth_service.service;

import com.coconut.auth_service.service.interfaces.UserServerService;
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

  private final Oauth2ProviderService oauth2ProviderService;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    String resourceProvider = userRequest.getClientRegistration().getRegistrationId();
    OAuth2User oAuth2User = super.loadUser(userRequest);

//    authUserServiceImpl.saveOauth2UserOnInHouseUser(resourceProvider, oAuth2User);

    return oAuth2User;
  }


}
