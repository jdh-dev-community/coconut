package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.PostCommentCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
  Page<PostCommentCountDto> findAllPostWithCommentCount(Pageable pageable);

  Post findByIdWithException(long postId);
}
