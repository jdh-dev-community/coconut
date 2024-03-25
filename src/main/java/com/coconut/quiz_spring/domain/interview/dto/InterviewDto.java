package com.coconut.quiz_spring.domain.interview.dto;

import com.coconut.quiz_spring.domain.interview.domain.Interview;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "인터뷰 응답 객체")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterviewDto {

  @Schema(description = "인터뷰의 id", example = "1")
  private long interviewId;

  @Schema(description = "인터뷰 제목", example = "백엔드 주니어 채용 공고")
  private String title;

  @Schema(description = "자격요건", example = "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 개발경험")
  private String requirements;

  @Schema(description = "우대사항", example = "클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험 \n 개발경험")
  private String preferred;

  @Schema(description = "스택", example = "#spring boot")
  private String stack;

  @Schema(description = "아이콘", example = "/springboot (s3 path)")
  private String icon;

  @Schema(description = "최근 수정일", example = "2023-01-01T12:00:00")
  private LocalDateTime updatedAt;

  public static InterviewDto of (long interviewId, String title, String requirements, String preferred, String stack, String icon, LocalDateTime updatedAt) {
    return InterviewDto.builder()
            .interviewId(interviewId)
            .title(title)
            .requirements(requirements)
            .preferred(preferred)
            .stack(stack)
            .icon(icon)
            .updatedAt(updatedAt)
            .build();
  }

  public static InterviewDto from (Interview interview) {
    return InterviewDto.of(
            interview.getInterviewId(),
            interview.getTitle(),
            interview.getRequirements(),
            interview.getPreferred(),
            interview.getStack(),
            interview.getIcon(),
            interview.getUpdatedAt()
    );
  }

  @Override
  public String toString() {
    return "InterviewDto{" +
            "interviewId=" + interviewId +
            ", title='" + title + '\'' +
            ", requirements='" + requirements + '\'' +
            ", preferred='" + preferred + '\'' +
            ", stack='" + stack + '\'' +
            ", icon='" + icon + '\'' +
            ", updatedAt=" + updatedAt +
            '}';
  }
}
