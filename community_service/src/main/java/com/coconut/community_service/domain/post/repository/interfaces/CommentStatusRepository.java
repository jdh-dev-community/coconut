package com.coconut.community_service.domain.post.repository.interfaces;

import com.coconut.community_service.domain.post.domain.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentStatusRepository extends JpaRepository<CommentStatus, Long> {
  Optional<CommentStatus> findByCommentStatus(String status);
}
