package com.coconut.quiz_spring.domain.interview.domain;

import com.coconut.quiz_spring.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "interviews")
public class Interview  extends BaseEntity {

  @Schema(description = "id", example = "1")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "interview_id")
  private long interviewId;

  @Schema(description = "인터뷰 제목", example = "백엔드 주니어 채용 공고")
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

  @Builder
  public Interview(long interviewId, String title, String requirements, String preferred, String stack, String icon) {
    this.interviewId = interviewId;
    this.title = title;
    this.requirements = requirements;
    this.preferred = preferred;
    this.stack = stack;
    this.icon = icon;
  }
}
