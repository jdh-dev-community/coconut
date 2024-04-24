package com.coconut.quiz_spring.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)

// TODO: hasNext, currentPage 등 Page 인스턴스의 다른 정보들을 추가
public class ListResDto<T> {

  @Schema(description = "해당 조건을 충족하는 테이블 내의 전체 데이터 row 수", example = "1000")
  private long elementsCount;

  @Schema(description = "조회된 목록")
  private List<T> content;

  public static <T>  ListResDto<T> of (long count, List<T> content) {
    return new ListResDto<>(count, content);
  }

}
