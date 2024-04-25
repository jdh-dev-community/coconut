package com.jdh.community_spring.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
// TODO: hasNext, currentPage 등 Page 인스턴스의 다른 정보들을 추가
public class ListResDto<T> {

  @Schema(description = "전체 게시글의 개수", example = "1000")
  private long elementsCount;

  @Schema(description = "List 타입의 게시글")
  private List<T> content;

}
