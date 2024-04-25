package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
  @Query(value = "SELECT c FROM Comment c JOIN FETCH c.post WHERE c.parentComment.commentId = :parentId",
          countQuery = "SELECT COUNT(c) FROM Comment c WHERE c.parentComment.commentId = :parentId")
  Page<Comment> findAllByParentCommentId(long parentId, Pageable pageable);
}
