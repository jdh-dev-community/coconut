package com.jdh.community_spring.domain.post.dto;


import com.jdh.community_spring.common.constant.CommentStatusKey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@RequiredArgsConstructor
public class CommentCreateReqDto {
  @Schema(description = "게시글의 내용", example = "When I start my server, the error below shows")
  @NotBlank(message = "내용은 필수 입력 값입니다.")
  private final String content;

  @Schema(description = "댓글의 작성자", example = "jack")
  @NotNull(message = "작성자는 필수 입력 값입니다.")
  private final String creator;

  @Schema(description = "댓글 비밀번호", example = "1234")
  @NotNull(message = "비밀번호는 필수 입력값입니다.")
  @Size(min = 4)
  private final String password;

  @Schema(description = "대댓글인 경우 부모 댓글의 id", example = "1234", nullable = true)
  private final Long parentId;

  @Schema(description = "댓글의 상태 (active/inactive/blocked)", example = "active", nullable = true)
  private final CommentStatusKey status = CommentStatusKey.ACTIVE;

  @Override
  public String toString() {
    return "CommentCreateReqDto{" +
            "content='" + content + '\'' +
            ", creator='" + creator + '\'' +
            ", password='" + password + '\'' +
            ", status='" + status + '\'' +
            ", parentId=" + parentId +
            '}';
  }
}
