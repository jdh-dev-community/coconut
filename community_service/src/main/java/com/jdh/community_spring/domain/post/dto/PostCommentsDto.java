package com.jdh.community_spring.domain.post.dto;

import com.jdh.community_spring.common.constant.PostCategory;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCommentsDto {
  @Schema(description = "게시글의 id", example = "1")
  private long postId;

  @Schema(description = "게시글의 제목", example = "Why this error occurs?")
  private String title;

  @Schema(description = "게시글의 내용", example = "When I start my server, the error below shows")
  private String content;

  @Schema(description = "게시글의 카테고리", example = "질문, 홍보, 상담")
  private PostCategory category;

  @Schema(description = "게시글의 작성자", example = "jack")
  private String creator;

  @Schema(description = "게시글의 조회수", example = "100")
  @NotNull(message = "조회수는 필수 입력 값입니다.")
  private long viewCount;

  @Schema(description = "게시글에 달린 댓글 목록", example = "{elementsCount: 4, content: 댓글[]}")
  ListResDto<CommentDto> comments;

  @Schema(description = "게시글의 생성일자", example = "2023-01-01T12:00:00")
  private LocalDateTime createdAt;

  public static PostCommentsDto of (Post post, ListResDto<CommentDto> comments) {
    return PostCommentsDto.builder()
            .postId(post.getPostId())
            .title(post.getTitle())
            .content(post.getTextContent())
            .category(PostCategory.match((post.getCategory())))
            .creator(post.getCreator())
            .viewCount(post.getViewCount())
            .comments(comments)
            .createdAt(post.getCreatedAt())
            .build();
  }
}
