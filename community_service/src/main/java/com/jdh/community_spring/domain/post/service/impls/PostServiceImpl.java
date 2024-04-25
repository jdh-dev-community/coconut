package com.jdh.community_spring.domain.post.service.impls;

import com.jdh.community_spring.common.constant.OrderBy;
import com.jdh.community_spring.common.constant.SortBy;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.domain.mapper.PostMapper;
import com.jdh.community_spring.domain.post.dto.*;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.CommentService;
import com.jdh.community_spring.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  private final InMemoryDBProvider inMemoryDBProvider;

  private final CommentService commentService;

  private final SimpleEncrypt simpleEncrypt;

  private final PostMapper postMapper;

  @Override
  public ListResDto<PostCommentCountDto> getPostList(ListReqDto listReqDto) {
    Pageable pageable = listReqDto.toPageable();
    Page<PostCommentCountDto> post = postRepository.findAllPostWithCommentCount(pageable);

    return new ListResDto<>(post.getTotalElements(), post.getContent());
  }

  @Transactional
  @Override
  public PostCommentsDto getPost(long postId) {
    Post post = postRepository.findByIdInPessimisticWrite(postId)
            .orElseThrow(() -> new EntityNotFoundException("[postId: " + postId + "] 게시글이 존재하지 않습니다"));

    post.updateViewCount();
    postRepository.save(post);

    ListReqDto dto = ListReqDto.of(1, 10, SortBy.RECENT, OrderBy.DESC);
    ListResDto<CommentDto> comments = commentService.getCommentList(postId, dto);

    return PostCommentsDto.of(post, comments);
  }

  @Override
  public PostCommentCountDto createPost(PostCreateReqDto dto) {
    try {
      Post post = postMapper.from(dto);
      Post savedPost = postRepository.save(post);
      return PostCommentCountDto.from(savedPost);
    } catch (Exception ex) {
      log.error("입력값: {}, 메세지: {}", dto, ex.getMessage());
      throw ex;
    }
  }

  @Override
  public TokenResDto generateToken(PostTokenReqDto dto) {

    Post post = postRepository.findByIdWithException(dto.getPostId());
    boolean isValidPassword = simpleEncrypt.match(dto.getPassword(), post.getPassword());

    if (isValidPassword) {
      String token = simpleEncrypt.encrypt(dto.getPostId() + dto.getPassword());
      inMemoryDBProvider.setTemperarily(String.valueOf(dto.getPostId()), token, 3 * 60);
      return new TokenResDto(token);
    } else {
      throw new IllegalArgumentException("잘못된 비밀번호입니다. 비밀번호를 확인해주세요");
    }
  }



  @Transactional
  @Override
  public PostCommentCountDto editPost(long postId, PostEditReqDto dto) {
    Post post = postRepository.findByIdWithException(postId);

    post.updatePost(
            dto.getTitle(),
            dto.getContent(),
            dto.getCategory()
    );

    postRepository.save(post);

    return PostCommentCountDto.from(post);
  }

  @Override
  public void deletePost(long postId) {
    postRepository.deleteById(postId);
  }


}




