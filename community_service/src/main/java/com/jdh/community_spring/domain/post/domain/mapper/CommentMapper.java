package com.jdh.community_spring.domain.post.domain.mapper;

import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.CommentStatus;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {
  private final SimpleEncrypt simpleEncrypt;

  public Comment of(CommentCreateReqDto dto, Post post, Comment parentComment, CommentStatus status) {

    return Comment.builder()
            .content(dto.getContent())
            .creator(dto.getCreator())
            .password(simpleEncrypt.encrypt(dto.getPassword()))
            .parentComment(parentComment)
            .post(post)
            .commentStatus(status)
            .build();
  }
}
