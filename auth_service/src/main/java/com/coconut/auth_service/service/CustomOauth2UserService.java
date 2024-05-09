package com.coconut.auth_service.service;

import com.coconut.auth_service.dto.CustomOauth2User;
import com.coconut.auth_service.factory.oauth2.factory.Oauth2ProviderFactory;
import com.coconut.auth_service.factory.oauth2.interfaces.OauthProvider;
import com.coconut.auth_service.service.interfaces.UserServerService;
import com.coconut.global.dto.UserCreateReqDto;
import com.coconut.global.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


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

    UserDto coconutUser = syncWithServiceUser(resourceProvider, oAuth2User);
    CustomOauth2User customOauth2User = convertToCustomOauth2User(oAuth2User,coconutUser);

    return customOauth2User;
  }

  private CustomOauth2User convertToCustomOauth2User(OAuth2User oAuth2User,  UserDto coconutUser) {



    CustomOauth2User user = CustomOauth2User.builder()
            .attributes(oAuth2User.getAttributes())
            .authorities(oAuth2User.getAuthorities())
            .name(oAuth2User.getName())
            .userId(coconutUser.getUserId())
            .build();

    return user;
  }

  private UserDto syncWithServiceUser(String resourceProvider, OAuth2User user) {
    OauthProvider provider = oauth2ProviderFactory.getOauth2AuthUserProvider(resourceProvider);
    UserCreateReqDto signInUserDetails = provider.parseOAuth2User(user);
    return userServerService.upsertUser(signInUserDetails);
  }

}
