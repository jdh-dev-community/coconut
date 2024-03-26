package com.coconut.quiz_spring.domain.jobposting.domain;

import com.coconut.quiz_spring.common.domain.BaseEntity;
import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "jobpostings")
public class JobPosting extends BaseEntity {

  @Schema(description = "id", example = "1")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "jobposting_id")
  private long jobPostingId;

  @Schema(description = "채용공고 제목", example = "백엔드 주니어 채용 공고")
  @Column(name = "title")
  private String title;

  @Schema(description = "자격요건", example = "자바 스프링 프레임워크(Spring Framework)를 이용한 개발 경험 \n 개발경험")
  @Column(name = "requirements")
  @Lob
  private String requirements;

  @Schema(description = "우대사항", example = "클라우드 서비스(AWS, Azure, Google Cloud Platform) 경험 \n 개발경험")
  @Column(name = "preferred")
  @Lob
  private String preferred;

  @Schema(description = "스택", example = "spring boot")
  @Column(name = "stack")
  private String stack;

  @Schema(description = "아이콘", example = "/springboot (s3 path)")
  @Column(name = "icon_path")
  private String icon;

  @Schema(description = "조회수", example = "100")
  @Column(name = "view_count")
  private long viewCount;

  @Schema(description = "상태", example = "active(노출), inactive(비노출), deleted(삭제)")
  @Column(name = "status")
  private String status;


  public void updateViewCount(long updatedViewCount) {
    this.viewCount = updatedViewCount;
  }


  public JobPostingStatus getStatus() {
    return JobPostingStatus.findBy(status);
  }

  public void updateStatus(JobPostingStatus status) {
    this.status = status.getValue();
  }

  @Builder
  public JobPosting(long jobPostingId, String title, String requirements, String preferred, String stack, String icon, long viewCount, JobPostingStatus status) {
    this.jobPostingId = jobPostingId;
    this.title = title;
    this.requirements = requirements;
    this.preferred = preferred;
    this.stack = stack;
    this.icon = icon;
    this.viewCount = viewCount;
    this.status = status.getValue();
  }
}
