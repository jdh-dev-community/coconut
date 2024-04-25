package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.domain.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentStatusRepository extends JpaRepository<CommentStatus, Long> {
  Optional<CommentStatus> findByCommentStatus(String status);
}
