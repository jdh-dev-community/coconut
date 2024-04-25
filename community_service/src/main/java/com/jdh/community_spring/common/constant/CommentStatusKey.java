package com.jdh.community_spring.common.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum CommentStatusKey {
  ACTIVE("active"),
  INACTIVE("inactive"),
  BLOCKED("blocked");

  private final String commentStatus;

  CommentStatusKey(String status) {
    this.commentStatus = status;
  }

  @JsonValue
  public String getCommentStatus() { return this.commentStatus;}

  public static CommentStatusKey match(String status) {
    return Arrays.stream(CommentStatusKey.values())
            .filter((c) -> c.getCommentStatus().equalsIgnoreCase(status))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("활성화, 비활성화, 신고 중에서 선택해주세요. 입력: " + status));
  }

}
