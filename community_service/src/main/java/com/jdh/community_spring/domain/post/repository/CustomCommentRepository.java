package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomCommentRepository {
  Page<CommentDto> findCommentsByPostId(long postId, Pageable pageable);
  Comment findByIdWithException(long commentId);
}
