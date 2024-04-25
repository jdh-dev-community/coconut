package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.*;

public interface PostService {
  PostCommentCountDto createPost(PostCreateReqDto dto);
  ListResDto<PostCommentCountDto> getPostList(ListReqDto listReqDto);
  PostCommentsDto getPost(long postId);
  TokenResDto generateToken(PostTokenReqDto dto);
  PostCommentCountDto editPost(long postId, PostEditReqDto dto);
  void deletePost(long postId);
}
