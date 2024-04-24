package com.coconut.quiz_service.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ToString
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "openai")
public class OpenAiProperties {
  private final Api api;
  private final Quiz quiz;
  private final Answer answer;

  @Getter
  @ToString
  @AllArgsConstructor
  public static class Api {
    private final String key;
    private final String url;
    private final String model;
  }

  @Getter
  @ToString
  @AllArgsConstructor
  public static class Quiz {
    private final String guide;
    private final String prompt;
  }

  @Getter
  @ToString
  @AllArgsConstructor
  public static class Answer {
    private final String guide;;
  }


}