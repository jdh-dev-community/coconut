package com.coconut.global.converter;

import com.coconut.global.constant.SignInType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToSignInTypeConverter implements Converter<String, SignInType> {

  @Override
  public SignInType convert(String source) {
    return SignInType.match(source);
  }


}
