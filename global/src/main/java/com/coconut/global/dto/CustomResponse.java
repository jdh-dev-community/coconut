package com.coconut.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomResponse<T> {
  private T result;
  private HttpErrorInfo error;

  public static <T> CustomResponse<T> of (T data) {
    return new CustomResponse<>(data, null);
  }

  public static CustomResponse<HttpErrorInfo> of (HttpErrorInfo error) {
    return new CustomResponse<>(null, error);
  }
}
