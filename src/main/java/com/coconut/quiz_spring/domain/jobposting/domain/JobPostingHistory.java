package com.coconut.quiz_spring.domain.jobposting.domain;

import com.coconut.quiz_spring.common.domain.BaseEntity;
import com.coconut.quiz_spring.domain.jobposting.constants.JobPostingAction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "jobposting_histories")
public class JobPostingHistory extends BaseEntity {
  @Schema(description = "id", example = "1")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "jobposting_history_id")
  private long jobPostingHistoryId;

  @Schema(description = "채용 공고 id", example = "유저 id로 수정될 예정")
  private long jobPostingId;

  @Schema(description = "액션 생성자", example = "유저 id로 수정될 예정")
  @Column(name = "actor")
  private String actor;

  @Schema(description = "액션", example = "edit")
  @Column(name = "action")
  private String action;

  @Schema(description = "요청", example = "반영하려고한 데이터")
  @Column(name = "request")
  @Lob
  private String request;

  public JobPostingAction getAction() {
    return JobPostingAction.findBy(action);
  }

  @Builder
  public JobPostingHistory(long jobPostingHistoryId, long jobPostingId, String actor, JobPostingAction action, String request) {
    this.jobPostingHistoryId = jobPostingHistoryId;
    this.jobPostingId = jobPostingId;
    this.actor = actor;
    this.action = action.getValue();
    this.request = request;
  }
}
