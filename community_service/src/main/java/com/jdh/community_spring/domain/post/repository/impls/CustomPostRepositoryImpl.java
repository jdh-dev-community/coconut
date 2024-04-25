package com.jdh.community_spring.domain.post.repository.impls;


import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.domain.QComment;
import com.jdh.community_spring.domain.post.dto.PostCommentCountDto;
import com.jdh.community_spring.domain.post.repository.CustomBaseRepository;
import com.jdh.community_spring.domain.post.repository.CustomPostRepository;
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

import static com.jdh.community_spring.domain.post.domain.QPost.post;
import static com.jdh.community_spring.domain.post.domain.QComment.comment;

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
            .selectFrom(post)
            .where(post.postId.eq(postId))
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
            .where(parentComment.post.postId.eq(post.postId)
                    .and(parentComment.parentComment.isNull())
                    .and(parentComment.commentStatus.commentStatus.eq("active")
                            .or(parentComment.commentStatus.commentStatus.ne("active").and(hasReplies))));


    List<Tuple> results = jpaQueryFactory
            .select(
                    post.postId,
                    post.title,
                    post.textContent,
                    post.category,
                    post.creator,
                    post.viewCount,
                    commentCountSubQuery,
                    post.createdAt)
            .from(post)
            .leftJoin(comment).on(comment.post.postId.eq(post.postId))
            .groupBy(post.postId)
            .orderBy(extractOrder(pageable.getSort(), entityPath))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    List<PostCommentCountDto> dtos = results.stream()
            .map((result) -> PostCommentCountDto.of(
                    result.get(post.postId),
                    result.get(post.title),
                    result.get(post.textContent),
                    result.get(post.category),
                    result.get(post.creator),
                    result.get(post.viewCount),
                    result.get(commentCountSubQuery),
                    result.get(post.createdAt)
            )).collect(Collectors.toList());

    long count = jpaQueryFactory
            .select(post.count())
            .from(post)
            .fetchOne();

    return new PageImpl<>(dtos, pageable, count);
  }
}
