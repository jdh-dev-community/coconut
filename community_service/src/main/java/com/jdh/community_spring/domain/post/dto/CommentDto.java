package com.jdh.community_spring.domain.post.dto;

import com.jdh.community_spring.common.constant.CommentStatusKey;
import com.jdh.community_spring.domain.post.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDto {
  @Schema(description = "댓글의 id", example = "1")
  private long commentId;

  @Schema(description = "댓글의 내용", example = "Why this error occurs?")
  private String content;

  @Schema(description = "댓글의 작성자", example = "jack")
  private String creator;

  @Schema(description = "댓글의 생성일자", example = "2023-01-01T12:00:00")
  private LocalDateTime createdAt;

  @Schema(description = "대댓글의 숫자", example = "0")
  private long childrenCommentCount;

  @Schema(description = "게시글의 id", example = "1")
  private long postId;

  @Schema(description = "댓글의 상태 ", example = "active")
  private CommentStatusKey status;

  // NOTE: 부모 댓글을 처리하기 위한 메소드
  public static CommentDto of(long commentId, String content, String creator, LocalDateTime createdAt, long childrenCommentCount, long postId, CommentStatusKey status) {
    return CommentDto.builder()
            .commentId(commentId)
            .content(content)
            .creator(creator)
            .createdAt(createdAt)
            .childrenCommentCount(childrenCommentCount)
            .postId(postId)
            .status(status)
            .build();
  }

  // NOTE: 대댓글을 처리하기 위한 메소드
  public static CommentDto from(Comment comment) {
    return CommentDto.builder()
            .commentId(comment.getCommentId())
            .content(comment.getContent())
            .creator(comment.getCreator())
            .createdAt(comment.getUpdatedAt())
            .postId(comment.getPost().getPostId())
            .status(CommentStatusKey.match(comment.getCommentStatus().getCommentStatus()))
            .build();
  }

  @Override
  public String toString() {
    return "CommentDto{" +
            "commentId=" + commentId +
            ", content='" + content + '\'' +
            ", creator='" + creator + '\'' +
            ", createdAt=" + createdAt +
            ", childrenCommentCount=" + childrenCommentCount +
            ", postId=" + postId +
            ", status=" + status +
            '}';
  }
}
