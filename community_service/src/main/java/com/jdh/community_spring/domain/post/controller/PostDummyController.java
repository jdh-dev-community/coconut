package com.jdh.community_spring.domain.post.controller;

import com.jdh.community_spring.common.constant.PostCategory;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dummy")
public class PostDummyController {

  private final SimpleEncrypt simpleEncrypt;

  private final PostRepository postRepository;


  @Operation(summary = "게시글 더미 생성", description = "요청하는 size 만큼 게시글 더미를 생성하는 api 입니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post")
  public void createPost(@RequestBody Map<String, Integer> body) {

    Integer size = body.get("size");
    List<Post> list = IntStream.rangeClosed(1, size)
            .mapToObj((i) -> Post.builder()
                    .title("제목" + i)
                    .textContent("이건 더미 데이터")
                    .creator("테스트")
                    .category(PostCategory.AD)
                    .password(simpleEncrypt.encrypt("1234"))
                    .build()
            ).collect(Collectors.toList());

    postRepository.saveAll(list);
  }

  @Operation(summary = "모든 게시글 삭제", description = "모든 게시글을 삭제하는 api 입니다.")
  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/post")
  public boolean deleteAllPost() {
    postRepository.deleteAll();
    return true;
  }
}
