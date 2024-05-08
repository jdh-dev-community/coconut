package com.coconut.auth_service.filter;

import com.coconut.auth_service.dto.IdPasswordSignInDto;
import com.coconut.auth_service.wrapper.CustomContentCachingRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.passay.*;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class IdPasswordSigninValidationFilter extends OncePerRequestFilter {

  private final EmailValidator emailValidator;
  private final PasswordValidator passwordValidator;

  public IdPasswordSigninValidationFilter() {
    this.emailValidator = EmailValidator.getInstance();
    this.passwordValidator = new PasswordValidator(
            new LengthRule(8, 16),
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            new CharacterRule(EnglishCharacterData.Digit, 1),
            new CharacterRule(EnglishCharacterData.Special, 1),
            new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
            new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false),
            new IllegalSequenceRule(EnglishSequenceData.USQwerty, 5, false),
            new WhitespaceRule());
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    CustomContentCachingRequestWrapper cachedRequest = (CustomContentCachingRequestWrapper) request;
    IdPasswordSignInDto dto = cachedRequest.getBody(IdPasswordSignInDto.class);

    boolean isValid = validateSigninInput(dto);
    if (isValid) {
      filterChain.doFilter(request, response);
    } else {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "입력한 내용을 다시 확인해주세요.");
    }
  }


  private boolean validateSigninInput(IdPasswordSignInDto dto) {
    String email = dto.getEmail();
    String password = dto.getPassword();
    String mobile = dto.getMobile();
    String nick = dto.getNickname();

    Boolean isValidEmail = emailValidator.isValid(email);
    Boolean isValidPw = passwordValidator.validate(new PasswordData(password)).isValid();

    boolean isNotEmpty = List.of(email, password, mobile, nick).stream().allMatch(Objects::nonNull);

    return isNotEmpty && isValidEmail && isValidPw;
  }


}
