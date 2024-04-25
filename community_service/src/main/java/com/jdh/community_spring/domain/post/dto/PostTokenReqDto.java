package com.jdh.community_spring.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class PostTokenReqDto {
  @Schema(description = "게시글의 id", example = "1")
  @Min(1)
  private final long postId;

  @Schema(description = "게시글의 비밀번호", example = "1234")
  @NotBlank(message = "비밀번호는 필수값입니다.")
  @Size(min = 4)
  private final String password;

  @Override
  public String toString() {
    return "PostTokenReqDto{" +
            "postId=" + postId +
            ", password='" + password + '\'' +
            '}';
  }
}
