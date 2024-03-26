package com.coconut.quiz_spring.domain.jobposting.dto;

import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Schema(description = "인터뷰 생성 요청 객체")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPostingCreateReq {

  @Schema(description = "채용공고 제목", example = "백엔드 주니어 채용 공고")
  @NotBlank(message = "제목은 필수 입력 값입니다.")
  private String title;

  @Schema(description = "자격요건", example = "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 개발경험")
  @NotBlank(message = "자격요건은 필수 입력 값입니다.")
  private String requirements;

  @Schema(description = "우대사항", example = "클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험 \n 개발경험")
  @NotBlank(message = "자격요건은 필수 입력 값입니다.")
  private String preferred;

  @Schema(description = "스택", example = "spring boot")
  @NotBlank(message = "자격요건은 필수 입력 값입니다.")
  private String stack;

  @Schema(description = "아이콘", example = "/springboot (s3 path)")
  @NotBlank(message = "자격요건은 필수 입력 값입니다.")
  private String icon;

  private final JobPostingStatus status = JobPostingStatus.ACTIVE;

  public static JobPostingCreateReq of (String title, String requirements, String preferred, String stack, String icon) {
    return JobPostingCreateReq.builder()
            .title(title)
            .requirements(requirements)
            .preferred(preferred)
            .stack(stack)
            .icon(icon)
            .build();
  }

  @Override
  public String toString() {
    return "JobPostingCreateReq{" +
            "title='" + title + '\'' +
            ", requirements='" + requirements + '\'' +
            ", preferred='" + preferred + '\'' +
            ", stack='" + stack + '\'' +
            ", icon='" + icon + '\'' +
            ", status=" + status +
            '}';
  }
}
