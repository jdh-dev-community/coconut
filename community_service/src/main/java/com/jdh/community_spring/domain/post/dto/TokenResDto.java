package com.jdh.community_spring.domain.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenResDto {

  @Schema(description = "3분간 유효한 토큰", example = "hash string")
  private final String token;
}
