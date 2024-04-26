package com.coconut.community_service.domain.post.service.interfaces;

import com.coconut.global.dto.ListReqDto;
import com.coconut.global.dto.ListResDto;
import com.coconut.community_service.domain.post.dto.CommentCreateReqDto;
import com.coconut.community_service.domain.post.dto.CommentDto;
import com.coconut.community_service.domain.post.dto.TokenReqDto;
import com.coconut.community_service.domain.post.dto.TokenResDto;

public interface CommentService {
  CommentDto createComment(long postId, CommentCreateReqDto dto);
  ListResDto<CommentDto> getCommentList(long postId, ListReqDto dto);
  ListResDto<CommentDto> getChildCommentList(long commentId, ListReqDto dto);
  TokenResDto generateToken(long commentId, TokenReqDto dto);
  CommentDto deleteComment(long commentId);

}
