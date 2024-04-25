package com.jdh.community_spring.common.constant;

import java.util.Arrays;

public enum SortBy {
  POPULAR("popular", "viewCount"),
  RECENT("recent", "createdAt");

  private final String fieldName;
  private final String sort;

  SortBy (String sort, String fieldName) {
    this.sort = sort.toLowerCase();
    this.fieldName = fieldName;
  }

  public String getSort() {
    return sort;
  }

  public String getFieldName() {
    return fieldName;
  }

  public static SortBy match(String sort) {
    return Arrays.stream(SortBy.values())
            .filter(sortBy -> sortBy.getSort().equalsIgnoreCase(sort))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("정의되지 않은 정렬 기준입니다. >> 입력:" + sort));
  }
}
