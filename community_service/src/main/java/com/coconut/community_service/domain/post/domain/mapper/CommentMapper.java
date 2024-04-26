package com.coconut.community_service.domain.post.domain.mapper;

import com.coconut.community_service.domain.post.domain.Comment;
import com.coconut.community_service.domain.post.domain.CommentStatus;
import com.coconut.community_service.domain.post.domain.Post;
import com.coconut.community_service.domain.post.dto.CommentCreateReqDto;
import com.coconut.community_service.common.util.SimpleEncrypt;
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
