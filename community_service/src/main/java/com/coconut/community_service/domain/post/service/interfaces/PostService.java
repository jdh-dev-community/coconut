package com.coconut.community_service.domain.post.service.interfaces;

import com.coconut.jpa_utils.dto.ListReqDto;
import com.coconut.jpa_utils.dto.ListResDto;
import com.coconut.community_service.domain.post.dto.*;

public interface PostService {
  PostCommentCountDto createPost(PostCreateReqDto dto);
  ListResDto<PostCommentCountDto> getPostList(ListReqDto listReqDto);
  PostCommentsDto getPost(long postId);
  TokenResDto generateToken(PostTokenReqDto dto);
  PostCommentCountDto editPost(long postId, PostEditReqDto dto);
  void deletePost(long postId);
}
