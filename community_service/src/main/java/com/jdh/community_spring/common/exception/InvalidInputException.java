package com.jdh.community_spring.common.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidInputException extends  RuntimeException {
  public InvalidInputException(String message) {
    super(message);
  }

  public InvalidInputException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidInputException(Throwable cause) {
    super(cause);
  }
}
