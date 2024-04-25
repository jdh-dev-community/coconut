package com.jdh.community_spring.domain.post.repository.impls;

import com.jdh.community_spring.common.constant.CommentStatusKey;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.dto.CommentDto;
import com.jdh.community_spring.domain.post.repository.CustomBaseRepository;
import com.jdh.community_spring.domain.post.repository.CustomCommentRepository;
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

import java.util.List;
import java.util.stream.Collectors;

import com.jdh.community_spring.domain.post.domain.QComment;

import javax.persistence.EntityNotFoundException;

import static com.jdh.community_spring.domain.post.domain.QComment.comment;

@Slf4j
public class CustomCommentRepositoryImpl implements CustomCommentRepository, CustomBaseRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final PathBuilder<Comment> entityPath;

  public CustomCommentRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
    this.entityPath = new PathBuilder<>(Comment.class, "comment");
  }

  @Override
  public Comment findByIdWithException(long commentId) {
    Comment selectedComment = jpaQueryFactory
            .selectFrom(comment)
            .where(comment.commentId.eq(commentId))
            .fetchOne();

    if (selectedComment == null) {
      throw new EntityNotFoundException("[commentId: " + commentId + "] 댓글이 존재하지 않습니다");
    }

    return selectedComment;
  }

  @Override
  public Page<CommentDto> findCommentsByPostId(long postId, Pageable pageable) {
    QComment childComment = new QComment("childComment");

    JPQLQuery<Long> commentCountSubQuery = JPAExpressions
            .select(childComment.count())
            .from(childComment)
            .where(
                    comment.commentId.eq(childComment.parentComment.commentId),
                    isValidComment(childComment)
            );


    List<Tuple> comments = jpaQueryFactory
            .select(
                    comment.commentId,
                    comment.content,
                    comment.creator,
                    comment.createdAt,
                    commentCountSubQuery,
                    comment.commentStatus.commentStatus
            ).from(comment)
            .leftJoin(comment.commentStatus)
            .where(
                    comment.post.postId.eq(postId),
                    comment.parentComment.isNull(),
                    isValidComment(childComment)
            )
            .orderBy(extractOrder(pageable.getSort(), entityPath))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    Long count = jpaQueryFactory
            .select(comment.count())
            .from(comment)
            .leftJoin(comment.commentStatus)
            .where(
                    comment.post.postId.eq(postId),
                    comment.parentComment.isNull(),
                    isValidComment(childComment)
            )
            .fetchOne();


    List<CommentDto> dtos = comments.stream()
            .map((result) -> CommentDto.of(
                    result.get(comment.commentId),
                    result.get(comment.content),
                    result.get(comment.creator),
                    result.get(comment.createdAt),
                    result.get(commentCountSubQuery),
                    postId,
                    CommentStatusKey.match(result.get(comment.commentStatus.commentStatus))
            )).collect(Collectors.toList());


    return new PageImpl<>(dtos, pageable, count);
  }

  private BooleanExpression isValidComment (QComment childComment) {
    BooleanExpression isActive = comment.commentStatus.commentStatus.eq("active");

    BooleanExpression hasReplies = JPAExpressions
            .selectOne()
            .from(childComment)
            .where(childComment.parentComment.commentId.eq(comment.commentId))
            .exists();

    BooleanExpression isInactiveWithReplies = comment.commentStatus.commentStatus.ne("active")
            .and(hasReplies);

    return isActive.or(isInactiveWithReplies);
  }

}
