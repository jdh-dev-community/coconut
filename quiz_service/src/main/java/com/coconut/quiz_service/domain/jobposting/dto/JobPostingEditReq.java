package com.coconut.quiz_service.domain.jobposting.dto;

import com.coconut.quiz_service.domain.jobposting.constants.JobPostingStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Schema(description = "인터뷰 수정 요청 객체")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPostingEditReq {
  @Schema(description = "채용공고 제목", example = "백엔드 주니어 채용 공고")
  private String title;

  @Schema(description = "자격요건", example = "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 개발경험")
  private String requirements;

  @Schema(description = "우대사항", example = "클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험 \n 개발경험")
  private String preferred;

  @Schema(description = "스택", example = "#spring boot")
  private String stack;

  @Schema(description = "아이콘", example = "/springboot (s3 path)")
  private String icon;

  @Schema(description = "채용공고 상태", example = "active, inactive, deleted")
  private JobPostingStatus status;

  public boolean isEmpty() {
    List<Object> checkList = Arrays.asList(title, requirements, preferred, stack, icon, status);
    return checkList.stream().noneMatch(Objects::nonNull);
  }

  @JsonCreator
  public static JobPostingEditReq of (String title, String requirements, String preferred, String stack, String status, String icon) {
    return JobPostingEditReq.builder()
            .title(title)
            .requirements(requirements)
            .preferred(preferred)
            .stack(stack)
            .icon(icon)
            .status(status != null ? JobPostingStatus.findBy(status) : null)
            .build();
  }

  @Override
  public String toString() {
    return "JobPostingEditReq{" +
            "title='" + title + '\'' +
            ", requirements='" + requirements + '\'' +
            ", preferred='" + preferred + '\'' +
            ", stack='" + stack + '\'' +
            ", icon='" + icon + '\'' +
            ", status=" + status +
            '}';
  }
}
