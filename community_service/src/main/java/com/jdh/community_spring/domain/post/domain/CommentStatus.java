package com.jdh.community_spring.domain.post.domain;

import com.jdh.community_spring.common.constant.CommentStatusKey;
import com.jdh.community_spring.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comment_status")
public class CommentStatus extends BaseEntity {
  @Schema(description = "status id", example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_status_id")
  private long commentStatusId;

  @Schema(description = "상태 (활성화, 비활성화, 신고 등등)", example = "active")
  @Column(name = "comment_status", nullable = false, unique = true)
  private String commentStatus;

  @Builder
  public CommentStatus(
          long commentStatusId,
          CommentStatusKey status
  ) {
    this.commentStatusId = commentStatusId;
    this.commentStatus = status.getCommentStatus();
  }
}
