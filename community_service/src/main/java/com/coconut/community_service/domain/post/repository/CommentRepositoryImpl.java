package com.coconut.community_service.domain.post.repository;

import com.coconut.community_service.domain.post.domain.Comment;
import com.coconut.community_service.domain.post.domain.QComment;
import com.coconut.community_service.domain.post.dto.CommentDto;
import com.coconut.community_service.common.constant.CommentStatusKey;
import com.coconut.community_service.domain.post.repository.interfaces.CustomBaseRepository;
import com.coconut.community_service.domain.post.repository.interfaces.CustomCommentRepository;
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
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Repository
public class CommentRepositoryImpl implements CustomCommentRepository, CustomBaseRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final PathBuilder<Comment> entityPath;

  public CommentRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
    this.entityPath = new PathBuilder<>(Comment.class, "comment");
  }

  @Override
  public Comment findByIdWithException(long commentId) {
    Comment selectedComment = jpaQueryFactory
            .selectFrom(QComment.comment)
            .where(QComment.comment.commentId.eq(commentId))
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
                    QComment.comment.commentId.eq(childComment.parentComment.commentId),
                    isValidComment(childComment)
            );


    List<Tuple> comments = jpaQueryFactory
            .select(
                    QComment.comment.commentId,
                    QComment.comment.content,
                    QComment.comment.creator,
                    QComment.comment.createdAt,
                    commentCountSubQuery,
                    QComment.comment.commentStatus.commentStatus
            ).from(QComment.comment)
            .leftJoin(QComment.comment.commentStatus)
            .where(
                    QComment.comment.post.postId.eq(postId),
                    QComment.comment.parentComment.isNull(),
                    isValidComment(childComment)
            )
            .orderBy(extractOrder(pageable.getSort(), entityPath))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    Long count = jpaQueryFactory
            .select(QComment.comment.count())
            .from(QComment.comment)
            .leftJoin(QComment.comment.commentStatus)
            .where(
                    QComment.comment.post.postId.eq(postId),
                    QComment.comment.parentComment.isNull(),
                    isValidComment(childComment)
            )
            .fetchOne();


    List<CommentDto> dtos = comments.stream()
            .map((result) -> CommentDto.of(
                    result.get(QComment.comment.commentId),
                    result.get(QComment.comment.content),
                    result.get(QComment.comment.creator),
                    result.get(QComment.comment.createdAt),
                    result.get(commentCountSubQuery),
                    postId,
                    CommentStatusKey.match(result.get(QComment.comment.commentStatus.commentStatus))
            )).collect(Collectors.toList());


    return new PageImpl<>(dtos, pageable, count);
  }

  private BooleanExpression isValidComment (QComment childComment) {
    BooleanExpression isActive = QComment.comment.commentStatus.commentStatus.eq("active");

    BooleanExpression hasReplies = JPAExpressions
            .selectOne()
            .from(childComment)
            .where(childComment.parentComment.commentId.eq(QComment.comment.commentId))
            .exists();

    BooleanExpression isInactiveWithReplies = QComment.comment.commentStatus.commentStatus.ne("active")
            .and(hasReplies);

    return isActive.or(isInactiveWithReplies);
  }

}
