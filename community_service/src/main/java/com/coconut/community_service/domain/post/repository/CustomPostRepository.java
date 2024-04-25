package com.coconut.community_service.domain.post.repository;

import com.coconut.community_service.domain.post.domain.Post;
import com.coconut.community_service.domain.post.dto.PostCommentCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
  Page<PostCommentCountDto> findAllPostWithCommentCount(Pageable pageable);

  Post findByIdWithException(long postId);
}
