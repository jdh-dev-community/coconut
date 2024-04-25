package com.coconut.community_service.domain.post.service;

import com.coconut.community_service.common.dto.ListReqDto;
import com.coconut.community_service.common.dto.ListResDto;
import com.coconut.community_service.domain.post.dto.*;

public interface PostService {
  PostCommentCountDto createPost(PostCreateReqDto dto);
  ListResDto<PostCommentCountDto> getPostList(ListReqDto listReqDto);
  PostCommentsDto getPost(long postId);
  TokenResDto generateToken(PostTokenReqDto dto);
  PostCommentCountDto editPost(long postId, PostEditReqDto dto);
  void deletePost(long postId);
}
