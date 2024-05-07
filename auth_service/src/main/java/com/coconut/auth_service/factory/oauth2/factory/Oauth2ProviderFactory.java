package com.coconut.auth_service.factory.oauth2.factory;

import com.coconut.auth_service.factory.oauth2.interfaces.OauthProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class Oauth2ProviderFactory {
  private final Map<String, OauthProvider> providers = new HashMap<>();

  public Oauth2ProviderFactory(OauthProvider... authProviders) {
    for (OauthProvider provider : authProviders) {
      providers.put(provider.getProviderName(), provider);
    }
  }

  public OauthProvider getOauth2AuthUserProvider(String provider) {
    if (!providers.containsKey(provider)) {
      throw new RuntimeException("주어진 프로바이더명에 맞는 프로바이더가 존재하지 않습니다.");
    }

    return providers.get(provider);
  }
}
