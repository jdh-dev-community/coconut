package com.jdh.community_spring.domain.post.service.impls;


import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.CommentStatus;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.domain.mapper.CommentMapper;
import com.jdh.community_spring.domain.post.dto.*;
import com.jdh.community_spring.domain.post.repository.CommentRepository;
import com.jdh.community_spring.domain.post.repository.CommentStatusRepository;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final PostRepository postRepository;

  private final CommentStatusRepository commentStatusRepository;

  private final CommentMapper commentMapper;

  private final SimpleEncrypt simpleEncrypt;

  private final InMemoryDBProvider inMemoryDBProvider;


  // 자식 댓글의 자식에 대한 로직은 처리되지 않고 있음
  // 대대댓글은 없다고 가정
  @Override
  public ListResDto<CommentDto> getChildCommentList(long commentId, ListReqDto dto) {
    Pageable pageable = dto.toPageable();
    Page<Comment> comments = commentRepository.findAllByParentCommentId(commentId, pageable);
    List<CommentDto> commentDtos = comments.stream().map(CommentDto::from).collect(Collectors.toList());

    return new ListResDto<>(comments.getTotalElements(), commentDtos);
  }

  @Override
  public ListResDto<CommentDto> getCommentList(long postId, ListReqDto dto) {
    Pageable pageable = dto.toPageable();
    Page<CommentDto> comments = commentRepository.findCommentsByPostId(postId, pageable);

    return new ListResDto<>(comments.getTotalElements(), comments.getContent());
  }

  @Override
  public CommentDto createComment(long postId, CommentCreateReqDto dto) {
    try {
      Post post = postRepository.findByIdWithException(postId);
      Comment parentComment = dto.getParentId() != null
              ? commentRepository.findByIdWithException(dto.getParentId())
              : null;

      CommentStatus status = commentStatusRepository.findByCommentStatus(dto.getStatus().getCommentStatus())
              .orElseThrow(() -> new EntityNotFoundException("[status: ] " + dto.getStatus().getCommentStatus() + " comment status를 찾지 못했습니다."));

      Comment comment = commentMapper.of(dto, post, parentComment, status);
      commentRepository.save(comment);

      return CommentDto.from(comment);
    } catch (Exception ex) {
      log.error("입력값: {}, 메세지: {}", dto, ex.getMessage());
      throw ex;
    }
  }

  @Override
  public TokenResDto generateToken(long commentId, TokenReqDto dto) {
    Comment comment = commentRepository.findByIdWithException(commentId);
    boolean isValidPassword = simpleEncrypt.match(dto.getPassword(), comment.getPassword());

    if (isValidPassword) {
      String token = simpleEncrypt.encrypt(commentId + dto.getPassword());
      inMemoryDBProvider.setTemperarily(String.valueOf(commentId), token, 3 * 60);
      return new TokenResDto(token);
    } else {
      throw new IllegalArgumentException("잘못된 비밀번호입니다. 비밀번호를 확인해주세요");
    }
  }

  @Transactional
  @Override
  public CommentDto deleteComment(long commentId) {
    Comment comment = commentRepository.findByIdWithException(commentId);
    CommentStatus commentStatus = commentStatusRepository.findByCommentStatus("inactive")
            .orElseThrow(() -> new EntityNotFoundException("[status: inactive] comment status를 찾지 못했습니다."));

    comment.updateComment(commentStatus);
    commentRepository.save(comment);

    return CommentDto.from(comment);
  }


}
