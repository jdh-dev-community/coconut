package com.coconut.quiz_spring.domain.jobposting.dto;

import com.coconut.quiz_spring.domain.jobposting.domain.JobPosting;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "인터뷰 응답 객체")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobPostingDto {

  @Schema(description = "채공공고 id", example = "1")
  private long jobPostingId;

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

  @Schema(description = "조회수", example = "100")
  private long viewCount;

  @Schema(description = "최근 수정일", example = "2023-01-01T12:00:00")
  private LocalDateTime updatedAt;

  public static JobPostingDto of (long jobPostingId, String title, String requirements, String preferred, String stack, String icon, long viewCount, LocalDateTime updatedAt) {
    return JobPostingDto.builder()
            .jobPostingId(jobPostingId)
            .title(title)
            .requirements(requirements)
            .preferred(preferred)
            .stack(stack)
            .icon(icon)
            .viewCount(viewCount)
            .updatedAt(updatedAt)
            .build();
  }

  public static JobPostingDto from (JobPosting jobPosting) {
    return JobPostingDto.of(
            jobPosting.getJobPostingId(),
            jobPosting.getTitle(),
            jobPosting.getRequirements(),
            jobPosting.getPreferred(),
            jobPosting.getStack(),
            jobPosting.getIcon(),
            jobPosting.getViewCount(),
            jobPosting.getUpdatedAt()
    );
  }

  @Override
  public String toString() {
    return "JobPostingDto{" +
            "jobPostingId=" + jobPostingId +
            ", title='" + title + '\'' +
            ", requirements='" + requirements + '\'' +
            ", preferred='" + preferred + '\'' +
            ", stack='" + stack + '\'' +
            ", icon='" + icon + '\'' +
            ", updatedAt=" + updatedAt +
            '}';
  }
}
