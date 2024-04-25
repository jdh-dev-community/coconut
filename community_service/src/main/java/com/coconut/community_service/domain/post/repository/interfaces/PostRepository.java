package com.coconut.community_service.domain.post.repository.interfaces;

import com.coconut.community_service.domain.post.domain.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Post p WHERE p.id = :postId")
  Optional<Post> findByIdInPessimisticWrite(@Param("postId") Long postId);
}
