package com.jdh.community_spring.common.constant;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum PostCategory {
  QUESTION("question"),
  CONSULTING("consulting"),
  AD("ad");

  private final String category;

  PostCategory(String category) {
    this.category = category.toLowerCase();
  }

  @JsonValue
  public String getCategory() {
    return this.category;
  }

  public static PostCategory match(String category) {
    return Arrays.stream(PostCategory.values())
            .filter((c) -> c.getCategory().equalsIgnoreCase(category))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("질문, 상담, 홍보 중에서 선택해주세요. 입력: " + category));
  }
}
