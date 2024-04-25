package com.jdh.community_spring.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class TokenReqDto {

  @Schema(description = "컨텐츠의 비밀번호", example = "1234")
  @NotBlank(message = "비밀번호는 필수값입니다.")
  @Size(min = 4)
  private final String password;

  public TokenReqDto (@JsonProperty("password")  String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "TokenReqDto{" +
            ", password='" + password + '\'' +
            '}';
  }
}
