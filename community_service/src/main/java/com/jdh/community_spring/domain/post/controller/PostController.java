package com.jdh.community_spring.domain.post.controller;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.*;
import com.jdh.community_spring.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostController {

  private final PostService postService;


  @Operation(summary = "게시글 목록", description = "게시글 목록을 페이지별로 불러올 수 있는 api 입니다.")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/post")
  public ListResDto<PostCommentCountDto> getPostList(@Valid @ModelAttribute ListReqDto listReqDto) {
    ListResDto<PostCommentCountDto> dto = postService.getPostList(listReqDto);
    return dto;
  }

  @Operation(summary = "게시글 상세", description = "게시글 id를 기반으로 상세 내용을 불러오는 api 입니다.")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/post/{id}")
  public PostCommentsDto getPost(@PathVariable("id") long postId) {
    PostCommentsDto post = postService.getPost(postId);
    return post;
  }

  @Operation(summary = "게시글 생성", description = "제목, 내용, 작성자, 카테고리를 포함하는 게시글을 작성합니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post")
  public PostCommentCountDto createPost(@Valid @RequestBody PostCreateReqDto dto) {
    PostCommentCountDto result = postService.createPost(dto);
    return result;
  }

  @Operation(summary = "게시글 비밀번호 인증", description = "게시글 수정과 삭제를 위한 토큰 발급 api 입니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post/token")
  public TokenResDto getAuthToken(@Valid @RequestBody PostTokenReqDto dto) {
    TokenResDto result = postService.generateToken(dto);
    return result;
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/post/{id}")
  @Operation(
          summary = "게시글 수정",
          description = "게시글 수정 api 입니다.",
          security = { @SecurityRequirement(name = "bearerAuth") }
  )
  public PostCommentCountDto editPost(@PathVariable("id") long postId, @Valid @RequestBody PostEditReqDto dto) {
    PostCommentCountDto result = postService.editPost(postId, dto);
    return result;
  }

  @Operation(
          summary = "게시글 삭제",
          description = "게시글 삭제 api 입니다.",
          security = { @SecurityRequirement(name = "bearerAuth") }
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/post/{id}")
  public void deletePost(@PathVariable("id") long postId) {
    postService.deletePost(postId);
  }

}
