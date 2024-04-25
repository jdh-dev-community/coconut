package com.jdh.community_spring.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;



@Schema(description = "에러 응답")
@Getter
public class HttpErrorInfo {

  @Schema(description = "에러 발생 시간", example = "2023-01-01T12:00:00")
  private final LocalDateTime timestamp;

  @Schema(description = "에러 발생 경로", example = "/api/v1/post")
  private final String path;

  @Schema(description = "HTTP 상태 코드", example = "400, 500")
  private final HttpStatus httpStatus;

  @Schema(description = "에러 메세지", example = "잘못된 입력입니다. 입력값을 확인해주세요")
  private final String message;

  public HttpErrorInfo(HttpStatus httpStatus, String path, String message) {
    timestamp = LocalDateTime.now();
    this.httpStatus = httpStatus;
    this.path = path;
    this.message = message;
  }
}
