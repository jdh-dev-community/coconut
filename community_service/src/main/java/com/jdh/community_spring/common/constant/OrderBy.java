package com.jdh.community_spring.common.constant;

import java.util.Arrays;

public enum OrderBy {
  DESC("desc"),
  ASC("asc");

  private final String order;

  OrderBy(String order) {
    this.order = order.toLowerCase();
  }

  public String getOrder() {
    return this.order;
  }

  public static OrderBy match(String order) {
    return Arrays.stream(OrderBy.values())
            .filter((orderBy) -> orderBy.getOrder().equalsIgnoreCase(order))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("오름차순 혹은 내림차순을 선택해주세요. 입력: " + order));
  }

}
