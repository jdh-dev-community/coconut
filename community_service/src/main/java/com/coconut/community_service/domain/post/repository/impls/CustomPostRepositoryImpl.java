package com.coconut.community_service.domain.post.repository.impls;


import com.coconut.community_service.domain.post.domain.Post;
import com.coconut.community_service.domain.post.domain.QComment;
import com.coconut.community_service.domain.post.domain.QPost;
import com.coconut.community_service.domain.post.dto.PostCommentCountDto;
import com.coconut.community_service.domain.post.repository.CustomBaseRepository;
import com.coconut.community_service.domain.post.repository.CustomPostRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CustomPostRepositoryImpl implements CustomPostRepository, CustomBaseRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final PathBuilder<Post> entityPath;

  public CustomPostRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
    this.entityPath = new PathBuilder<>(Post.class, "post");
  }

  @Override
  public Post findByIdWithException(long postId) {
    Post selectedPost = jpaQueryFactory
            .selectFrom(QPost.post)
            .where(QPost.post.postId.eq(postId))
            .fetchOne();

    if (selectedPost == null) {
      throw new EntityNotFoundException("[postId: " + postId + "] 게시글이 존재하지 않습니다");
    }

    return selectedPost;
  }


  @Override
  public Page<PostCommentCountDto> findAllPostWithCommentCount(Pageable pageable) {

    QComment parentComment = new QComment("parentComment");
    QComment childComment = new QComment("childComment");
    BooleanExpression hasReplies = JPAExpressions
            .selectOne()
            .from(childComment)
            .where(childComment.parentComment.commentId.eq(parentComment.commentId))
            .exists();


    JPQLQuery<Long> commentCountSubQuery = JPAExpressions
            .select(parentComment.count())
            .from(parentComment)
            .where(parentComment.post.postId.eq(QPost.post.postId)
                    .and(parentComment.parentComment.isNull())
                    .and(parentComment.commentStatus.commentStatus.eq("active")
                            .or(parentComment.commentStatus.commentStatus.ne("active").and(hasReplies))));


    List<Tuple> results = jpaQueryFactory
            .select(
                    QPost.post.postId,
                    QPost.post.title,
                    QPost.post.textContent,
                    QPost.post.category,
                    QPost.post.creator,
                    QPost.post.viewCount,
                    commentCountSubQuery,
                    QPost.post.createdAt)
            .from(QPost.post)
            .leftJoin(QComment.comment).on(QComment.comment.post.postId.eq(QPost.post.postId))
            .groupBy(QPost.post.postId)
            .orderBy(extractOrder(pageable.getSort(), entityPath))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    List<PostCommentCountDto> dtos = results.stream()
            .map((result) -> PostCommentCountDto.of(
                    result.get(QPost.post.postId),
                    result.get(QPost.post.title),
                    result.get(QPost.post.textContent),
                    result.get(QPost.post.category),
                    result.get(QPost.post.creator),
                    result.get(QPost.post.viewCount),
                    result.get(commentCountSubQuery),
                    result.get(QPost.post.createdAt)
            )).collect(Collectors.toList());

    long count = jpaQueryFactory
            .select(QPost.post.count())
            .from(QPost.post)
            .fetchOne();

    return new PageImpl<>(dtos, pageable, count);
  }
}
