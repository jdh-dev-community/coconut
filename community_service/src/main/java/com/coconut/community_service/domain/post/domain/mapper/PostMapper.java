package com.coconut.community_service.domain.post.domain.mapper;

import com.coconut.community_service.domain.post.domain.Post;
import com.coconut.community_service.domain.post.dto.PostCreateReqDto;
import com.coconut.community_service.common.service.HtmlSanitizeService;
import com.coconut.community_service.common.util.SimpleEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
  private final HtmlSanitizeService htmlSanitizeService;
  private final SimpleEncrypt simpleEncrypt;

  public Post from(PostCreateReqDto dto) {
    String sanitizedHtml = htmlSanitizeService.sanitize(dto.getContent());
    String hashedPassword = simpleEncrypt.encrypt(dto.getPassword());

    return Post.builder()
            .title(dto.getTitle())
            .textContent(sanitizedHtml)
            .creator(dto.getCreator())
            .category(dto.getCategory())
            .viewCount(0)
            .password(hashedPassword)
            .build();
  }
}
