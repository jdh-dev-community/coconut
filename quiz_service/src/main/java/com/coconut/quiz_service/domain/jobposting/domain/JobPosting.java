package com.coconut.quiz_service.domain.jobposting.domain;

import com.coconut.quiz_service.common.domain.BaseEntity;
import com.coconut.quiz_service.domain.jobposting.constants.JobPostingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

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

  @Version
  private long version;

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

  public JobPostingStatus getStatus() {
    return JobPostingStatus.findBy(status);
  }



  public void updateJobPosting(String title, String requirements, String preferred, String stack, String icon, JobPostingStatus status) {
    if (title != null) this.title = title;
    if (requirements != null) this.requirements = requirements;
    if (preferred != null) this.preferred = preferred;
    if (stack != null) this.stack = stack;
    if (icon != null) this.icon = icon;
    if (status != null) this.status = status.getValue();
  }
  public void updateViewCount(long updatedViewCount) {
    this.viewCount = updatedViewCount;
  }
  public void updateStatus(JobPostingStatus status) {
    this.status = status.getValue();
  }




}
