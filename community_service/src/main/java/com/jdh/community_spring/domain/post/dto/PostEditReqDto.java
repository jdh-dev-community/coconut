package com.jdh.community_spring.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.jdh.community_spring.common.constant.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostEditReqDto {
  @Schema(description = "게시글의 제목", example = "Why this error occurs?")
  @NotBlank(message = "제목은 필수 입력 값입니다.")
  private final String title;

  @Schema(description = "게시글의 내용", example = "When I start my server, the error below shows")
  @NotBlank(message = "내용은 필수 입력 값입니다.")
  private final String content;

  @Schema(description = "게시글의 카테고리 (질문, 홍보, 상담)", example = "question")
  @NotNull(message = "카테고리는 필수 입력 값입니다.")
  private final PostCategory category;

  @JsonCreator
  public static PostEditReqDto of (String title, String content, String category) {
    return PostEditReqDto.builder()
            .title(title)
            .content(content)
            .category(PostCategory.match(category))
            .build();
  }

  @Override
  public String toString() {
    return "PostEditReqDto{" +
            "title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", category='" + category + '\'' +
            '}';
  }
}
