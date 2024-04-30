package com.coconut.community_service.domain.post.service;


import com.coconut.community_service.domain.post.domain.Comment;
import com.coconut.community_service.domain.post.domain.CommentStatus;
import com.coconut.community_service.domain.post.domain.Post;
import com.coconut.community_service.domain.post.dto.CommentCreateReqDto;
import com.coconut.community_service.domain.post.dto.CommentDto;
import com.coconut.community_service.domain.post.dto.TokenReqDto;
import com.coconut.community_service.domain.post.dto.TokenResDto;
import com.coconut.community_service.domain.post.service.interfaces.CommentService;
import com.coconut.jpa_utils.dto.ListReqDto;
import com.coconut.jpa_utils.dto.ListResDto;
import com.coconut.community_service.common.provider.InMemoryDBProvider;
import com.coconut.community_service.common.util.SimpleEncrypt;
import com.coconut.community_service.domain.post.domain.mapper.CommentMapper;
import com.coconut.community_service.domain.post.repository.interfaces.CommentRepository;
import com.coconut.community_service.domain.post.repository.interfaces.CommentStatusRepository;
import com.coconut.community_service.domain.post.repository.interfaces.PostRepository;
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

    return ListResDto.of(comments.getTotalElements(), commentDtos);
  }

  @Override
  public ListResDto<CommentDto> getCommentList(long postId, ListReqDto dto) {
    Pageable pageable = dto.toPageable();
    Page<CommentDto> comments = commentRepository.findCommentsByPostId(postId, pageable);

    return ListResDto.of(comments.getTotalElements(), comments.getContent());
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
