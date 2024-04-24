package com.coconut.quiz_service.domain.quiz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceedOpenAiQuotaException extends RuntimeException{
  private final HttpStatus statusCode;
  public ExceedOpenAiQuotaException(String message, HttpStatus statusCode, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
  }
}
