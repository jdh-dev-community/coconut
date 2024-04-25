package com.jdh.community_spring.domain.post.domain;

import com.jdh.community_spring.common.constant.PostCategory;
import com.jdh.community_spring.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

  @Schema(description = "id", example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private long postId;

  @Schema(description = "게시글의 제목", example = "Why this error occurs?")
  @Column(name = "title", nullable = false)
  private String title;

  @Schema(description = "게시글의 내용", example = "When I start my server, the error below shows")
  @Column(name = "text_content", nullable = false, columnDefinition = "TEXT")
  private String textContent;


  @Schema(description = "게시글의 작성자", example = "jack")
  // TODO: 유저 테이블 생성 후 맵핑
  @Column(name = "creator", nullable = false)
  private String creator;


  @Schema(description = "게시글의 카테고리", example = "질문, 홍보, 상담")
  // TODO: 카테고리 정리 후 테이블로 생성
  @Column(name = "category", nullable = false)
  private String category;

  @Schema(description = "게시글의 조회수", example = "100")
  @Column(name = "view_count", nullable = false)
  private long viewCount;

  @Schema(description = "최소 4자리를 사용하는 게시글의 비밀번호", example = "1234")
  @Column(name = "password", nullable = false)
  private String password;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments;

  @Version
  private Long version;

  @Builder
  public Post(long postId, String title, String textContent, String creator, PostCategory category, long viewCount, String password, List<Comment> comments, Long version) {
    this.postId = postId;
    this.title = title;
    this.textContent = textContent;
    this.creator = creator;
    this.category = category.getCategory();
    this.viewCount = viewCount;
    this.password = password;
    this.comments = comments;
    this.version = version;
  }

  public void updatePost(String title, String textContent, PostCategory category) {
    List<String> inputs = List.of(title, textContent, category.getCategory());
    inputs.stream().filter(Objects::isNull).findFirst().ifPresent((n) -> {
      throw new IllegalArgumentException("잘못된 수정 요청입니다. 입력을 확인해주세요");
    });

    this.title = title;
    this.textContent = textContent;
    this.category = category.getCategory();
  }

  public void updateViewCount() {
    this.viewCount += 1;
  }
}
