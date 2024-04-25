package com.jdh.community_spring.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

  @Schema(description = "생성일자", example = "2023-01-01T12:00:00")
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

  @Schema(description = "최근수정일", example = "2023-01-01T12:00:00")
  @CreationTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();
}
