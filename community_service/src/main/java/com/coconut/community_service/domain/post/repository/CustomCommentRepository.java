package com.coconut.community_service.domain.post.repository;

import com.coconut.community_service.domain.post.domain.Comment;
import com.coconut.community_service.domain.post.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomCommentRepository {
  Page<CommentDto> findCommentsByPostId(long postId, Pageable pageable);
  Comment findByIdWithException(long commentId);
}
