package com.coconut.quiz_spring.common.dto;



import com.coconut.quiz_spring.common.constant.OrderBy;
import com.coconut.quiz_spring.common.constant.SortBy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListReqDto {
  private final static int MAX_PAGE_SIZE = 30;

  @Schema(description = "페이지 (1부터 시작)", example = "1")
  @Min(value = 1, message = "page는 최소 1 이상이어야 합니다.")
  private int page = 1;

  @Schema(description = "페이지 사이즈", example = "10")
  @Min(value = 1, message = "size 최소 1 이상이어야 합니다.")
  @Max(value = MAX_PAGE_SIZE, message = "size 최대 30 이하이어야 합니다.")
  private int size = 10;

  @Schema(description = "정렬 기준, 기본은 최신순 정렬입니다. (recent/popular)", example = "recent")
  private SortBy sortBy = SortBy.RECENT;


  @Schema(description = "오름차순/내림차순, 기본은 내림차순 정렬입니다. (desc/asc)", example = "desc")
  private OrderBy orderBy = OrderBy.DESC;

  @Schema(description = "검색어", example = "react")
  private String search;

  public void setSortBy(String sortBy) {
    this.sortBy = SortBy.match(sortBy);
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = OrderBy.match(orderBy);
  }

  public void setSize(String size) {
    int intSize = Integer.parseInt(size);
    this.size = intSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : intSize;
  }

  public static ListReqDto of (int page, int size, SortBy sort, OrderBy order, String search) {
    return new ListReqDto(page, size, sort, order, search);
  }

  @Override
  public String toString() {
    return "ListReqDto{" +
            "page=" + page +
            ", size=" + size +
            ", sortBy=" + sortBy +
            ", orderBy=" + orderBy +
            ", search=" + search  +
            '}';
  }

  // NOTE: 프론트에서는 page가 1부터 시작하고 있어서 서버에서 -1을 합니다.
  public Pageable toPageable () {
    int page = this.page - 1;
    int size = this.size;

    Sort sort = orderBy.getOrder().equalsIgnoreCase(OrderBy.DESC.getOrder())
            ? Sort.by(sortBy.getFieldName()).descending()
            : Sort.by(sortBy.getFieldName()).ascending();

    return PageRequest.of(page, size, sort);
  }
}
