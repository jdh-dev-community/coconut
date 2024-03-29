package com.coconut.quiz_spring.domain.quiz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidOpenAiKeyException extends RuntimeException{
  private final HttpStatus statusCode;
  public InvalidOpenAiKeyException(String message, HttpStatus statusCode, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
  }
}
