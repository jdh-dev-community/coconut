package com.jdh.community_spring.domain.post.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdh.community_spring.common.constant.CommentStatusKey;
import com.jdh.community_spring.common.constant.PostCategory;
import com.jdh.community_spring.common.filter.TokenFilter;
import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.CommentStatus;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.repository.CommentRepository;
import com.jdh.community_spring.domain.post.repository.CommentStatusRepository;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SimpleEncrypt simpleEncrypt;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentStatusRepository commentStatusRepository;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private InMemoryDBProvider inMemoryDBProvider;

  @Autowired
  private WebApplicationContext context;

  private String baseUrl;
  private Post post;

  private CommentStatus activeStatus;
  private CommentStatus inactiveStatus;
  private MockMvc mockMvc;
  private final String dummyPassword = "1234";

  @BeforeEach
  public void setup() {
    activeStatus = commentStatusRepository.save(createCommentStatus(CommentStatusKey.ACTIVE));
    inactiveStatus = commentStatusRepository.save(createCommentStatus(CommentStatusKey.INACTIVE));
    post = postRepository.save(createPost("post"));

    baseUrl = "/api/v1/post/" + post.getPostId() + "/comment";

    TokenFilter tokenFilter = new TokenFilter(inMemoryDBProvider);
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .addFilter(tokenFilter, "/api/v1/post/*")
            .build();
  }

  @AfterEach
  public void cleanup() {
    commentStatusRepository.deleteAll();
    postRepository.deleteAll();
    baseUrl = "";
  }

  @Nested
  class 댓글생성_테스트 {
    private Map<String, String> defaultRequest;

    @BeforeEach
    public void setup() {
      System.out.println("Local: >>");
      this.defaultRequest = Map.of(
              "content", "it is comment",
              "creator", "me",
              "password", "1234",
              "parentId", "null",
              "status", "inactive"
      );
    }

    @AfterEach
    public void cleanup() {
      commentRepository.deleteAll();
    }


    @Test
    public void 요청_데이터가_유효한_경우_생성된_댓글과_201_응답() throws Exception {
      Map<String, String> validRequest = defaultRequest;
      String validBody = objectMapper.writeValueAsString(validRequest);

      postAndVerify(validBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.commentId").exists())
              .andExpect(jsonPath("$.content", Matchers.equalTo(validRequest.get("content"))))
              .andExpect(jsonPath("$.creator", Matchers.equalTo(validRequest.get("creator"))))
              .andExpect(jsonPath("$.createdAt").exists())
              .andExpect(jsonPath("$.childrenCommentCount", Matchers.equalTo(0)))
              .andExpect(jsonPath("$.postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.status", Matchers.equalTo(validRequest.get("status"))))
              .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void 요청_데이터에_부모댓글id가_없는_경우_생성된_댓글과_201_응답() throws Exception {
      Map<String, String> validRequest = new HashMap<>(defaultRequest);
      validRequest.remove("parentId");
      String validBody = objectMapper.writeValueAsString(validRequest);

      postAndVerify(validBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.commentId").exists())
              .andExpect(jsonPath("$.content", Matchers.equalTo(validRequest.get("content"))))
              .andExpect(jsonPath("$.creator", Matchers.equalTo(validRequest.get("creator"))))
              .andExpect(jsonPath("$.createdAt").exists())
              .andExpect(jsonPath("$.childrenCommentCount", Matchers.equalTo(0)))
              .andExpect(jsonPath("$.postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.status", Matchers.equalTo(validRequest.get("status"))))
              .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void 요청_데이터에_댓글상태가_없으면_active로_생성된_댓글과_201_응답() throws Exception {
      Map<String, String> validRequest = new HashMap<>(defaultRequest);
      validRequest.remove("status");
      String validBody = objectMapper.writeValueAsString(validRequest);

      postAndVerify(validBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.commentId").exists())
              .andExpect(jsonPath("$.content", Matchers.equalTo(validRequest.get("content"))))
              .andExpect(jsonPath("$.creator", Matchers.equalTo(validRequest.get("creator"))))
              .andExpect(jsonPath("$.createdAt").exists())
              .andExpect(jsonPath("$.childrenCommentCount", Matchers.equalTo(0)))
              .andExpect(jsonPath("$.postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.status", Matchers.equalTo(CommentStatusKey.ACTIVE.getCommentStatus())))
              .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void 요청_데이터에_필수값이_누락된_경우_400_응답() throws Exception {
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.remove("content");
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody)
              .andExpect(status().isBadRequest());
    }

    @Test
    public void 부모댓글id가_유효하지_않은_경우_404_응답을_반환() throws Exception {
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.put("parentId", "1000000");
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody)
              .andExpect(status().isNotFound());
    }

    private ResultActions postAndVerify(String body) throws Exception {
      return mockMvc.perform(post(baseUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
      );
    }

  }

  @Nested
  class 댓글목록_테스트 {
    private final int LIST_COUNT = 40;

    @BeforeEach
    public void setup() {
      List<Comment> comments = IntStream.rangeClosed(1, LIST_COUNT)
              .mapToObj((i) -> createActiveComment("댓글" + i, null))
              .collect(Collectors.toList());

      commentRepository.saveAll(comments);
    }

    @AfterEach
    public void cleanup() {
      commentRepository.deleteAll();
    }

    @Test
    public void 목록조회_성공시_전체_댓글_개수와_페이지_사이즈_만큼의_목록을_반환하고_200_응답() throws Exception {
      int pageSize = 3;

      mockMvc.perform((get(baseUrl + "?size=" + pageSize)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(LIST_COUNT)))
              .andExpect(jsonPath("$.content.size()", Matchers.equalTo(pageSize)))
              .andExpect(jsonPath("$.content.[0].commentId").exists())
              .andExpect(jsonPath("$.content.[0].content").exists())
              .andExpect(jsonPath("$.content.[0].creator").exists())
              .andExpect(jsonPath("$.content.[0].createdAt").exists())
              .andExpect(jsonPath("$.content.[0].childrenCommentCount").exists())
              .andExpect(jsonPath("$.content.[0].postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.content.[0].status").exists());
    }

    @Test
    public void 조회되는_댓글의_상태는_기본적으로_active_하지만_대댓글이_있는_경우에는_inactive도_조회() throws Exception {
      int pageSize = 3;

      Comment inactiveComment = createInactiveComment("inactive1");
      commentRepository.save(inactiveComment);

      MockHttpServletResponse responseOnlyActiveComments = mockMvc
              .perform(get(baseUrl + "?size=" + pageSize))
              .andReturn()
              .getResponse();

      // inactive가 db에 있지만 조회 되지는 않음을 검증
      List<Long> activeCommentIds = extractIds(responseOnlyActiveComments);
      assertFalse(activeCommentIds.contains(inactiveComment.getCommentId()));

      // inactive 댓글에 자식 댓글 추가
      Comment commentOnInactiveComment = createActiveComment("test comment", inactiveComment);
      commentRepository.save(commentOnInactiveComment);

      MockHttpServletResponse responseWithInactiveComments = mockMvc
              .perform(get(baseUrl + "?size=" + pageSize))
              .andReturn()
              .getResponse();

      // inactive이지만 대댓글이 생김으로 인해 조회가 되는 것을 검증
      List<Long> commentIds = extractIds(responseWithInactiveComments);
      assertTrue(commentIds.contains(inactiveComment.getCommentId()));
    }

    @Test
    public void 해당_페이지에_데이터가_없는_경우_() throws Exception {
      int page = 100000;

      mockMvc.perform((get(baseUrl + "?page=" + page)))
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(LIST_COUNT)))
              .andExpect(jsonPath("$.content.size()", Matchers.equalTo(0)));
    }

    @Test
    public void 페이지가_주어지지_않는_경우_1페이지를_기본으로_로딩하고_200_응답() throws Exception {
      int DEFAULT_PAGE_NUMBER = 1;

      MockHttpServletResponse responseWithPageNumber = mockMvc
              .perform(get(baseUrl + "?page=" + DEFAULT_PAGE_NUMBER))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithPageNumber);


      MockHttpServletResponse responseWithoutPageNumber = mockMvc
              .perform(get(baseUrl))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutPageNumber);
      int status = responseWithoutPageNumber.getStatus();


      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 페이지_사이즈가_주어지지_않는_경우_사이즈10을_기본으로_로딩하고_200_응답() throws Exception {
      int DEFAULT_PAGE_SIZE = 10;

      MockHttpServletResponse responseWithPageSize = mockMvc
              .perform(get(baseUrl + "?size=" + DEFAULT_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithPageSize);

      MockHttpServletResponse responseWithoutPageSize = mockMvc
              .perform(get(baseUrl))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutPageSize);
      int status = responseWithoutPageSize.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 정렬기준이_주어지지_않는_경우_최신순으로_정렬하고_200_응답() throws Exception {
      String DEFAULT_SORT_BY = "recent";

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(baseUrl + "?sortBy=" + DEFAULT_SORT_BY))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(baseUrl))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 오름차순_내림차순이_별도로_주어지지_않은_경우_내림차순_정렬하고_200_응답() throws Exception {
      String DEFAULT_ORDER_BY = "desc";

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(baseUrl + "?orderBy=" + DEFAULT_ORDER_BY))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(baseUrl))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 페이지_사이즈가_30개_이상인_경우_최대_30개까지만_반환하고_200_응답() throws Exception {
      int MAX_PAGE_SIZE = 30;
      int EXCEEDED_PAGE_SIZE = 60;

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(baseUrl + "?size=" + MAX_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(baseUrl + "?size=" + EXCEEDED_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    private List<Long> extractIds(MockHttpServletResponse response) throws Exception {
      JsonNode rootNode = objectMapper.readTree(response.getContentAsString());
      JsonNode contentNodes = rootNode.path("content");

      List<Long> ids = new ArrayList<>();

      for (JsonNode node : contentNodes) {
        ids.add(node.path("commentId").asLong());
      }

      return ids;
    }

  }

  @Nested
  class 대댓글목록_테스트 {
    private final int LIST_COUNT = 40;
    private Comment parentComment;

    private String customUrl;

    @BeforeEach
    public void setup() {
      parentComment = createActiveComment("부모댓글", null);
      commentRepository.save(parentComment);

      List<Comment> comments = IntStream.rangeClosed(1, LIST_COUNT)
              .mapToObj((i) -> createActiveComment("자식댓글" + i, parentComment))
              .collect(Collectors.toList());

      commentRepository.saveAll(comments);

      customUrl = baseUrl + "/" + parentComment.getCommentId();
    }

    @AfterEach
    public void cleanup() {
      commentRepository.deleteAll();
    }

    @Test
    public void 목록조회_성공시_전체_댓글_개수와_페이지_사이즈_만큼의_목록을_반환하고_200_응답() throws Exception {
      int pageSize = 3;

      mockMvc.perform((get(customUrl + "?size=" + pageSize)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(LIST_COUNT)))
              .andExpect(jsonPath("$.content.size()", Matchers.equalTo(pageSize)))
              .andExpect(jsonPath("$.content.[0].commentId").exists())
              .andExpect(jsonPath("$.content.[0].content").exists())
              .andExpect(jsonPath("$.content.[0].creator").exists())
              .andExpect(jsonPath("$.content.[0].createdAt").exists())
              .andExpect(jsonPath("$.content.[0].childrenCommentCount").exists())
              .andExpect(jsonPath("$.content.[0].postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.content.[0].status").exists());
    }

    @Test
    public void 유효하지_않은_부모댓글id를_사용한_요청은_404_응답() throws Exception {
      String invalidUrl = baseUrl + "/" + 100000 + "/comment";

      mockMvc.perform((get(invalidUrl)))
              .andExpect(status().isNotFound());
    }

    @Test
    public void 해당_페이지에_데이터가_없는_경우_() throws Exception {
      int page = 100000;

      mockMvc.perform((get(customUrl + "?page=" + page)))
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(LIST_COUNT)))
              .andExpect(jsonPath("$.content.size()", Matchers.equalTo(0)));
    }

    @Test
    public void 페이지가_주어지지_않는_경우_1페이지를_기본으로_로딩하고_200_응답() throws Exception {
      int DEFAULT_PAGE_NUMBER = 1;

      MockHttpServletResponse responseWithPageNumber = mockMvc
              .perform(get(customUrl + "?page=" + DEFAULT_PAGE_NUMBER))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithPageNumber);


      MockHttpServletResponse responseWithoutPageNumber = mockMvc
              .perform(get(customUrl))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutPageNumber);
      int status = responseWithoutPageNumber.getStatus();


      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 페이지_사이즈가_주어지지_않는_경우_사이즈10을_기본으로_로딩하고_200_응답() throws Exception {
      int DEFAULT_PAGE_SIZE = 10;

      MockHttpServletResponse responseWithPageSize = mockMvc
              .perform(get(customUrl + "?size=" + DEFAULT_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithPageSize);

      MockHttpServletResponse responseWithoutPageSize = mockMvc
              .perform(get(customUrl))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutPageSize);
      int status = responseWithoutPageSize.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 정렬기준이_주어지지_않는_경우_최신순으로_정렬하고_200_응답() throws Exception {
      String DEFAULT_SORT_BY = "recent";

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(customUrl + "?sortBy=" + DEFAULT_SORT_BY))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(customUrl))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 오름차순_내림차순이_별도로_주어지지_않은_경우_내림차순_정렬하고_200_응답() throws Exception {
      String DEFAULT_ORDER_BY = "desc";

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(customUrl + "?orderBy=" + DEFAULT_ORDER_BY))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(customUrl))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 페이지_사이즈가_30개_이상인_경우_최대_30개까지만_반환하고_200_응답() throws Exception {
      int MAX_PAGE_SIZE = 30;
      int EXCEEDED_PAGE_SIZE = 60;

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(customUrl + "?size=" + MAX_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Long> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(customUrl + "?size=" + EXCEEDED_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Long> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    private List<Long> extractIds(MockHttpServletResponse response) throws Exception {
      JsonNode rootNode = objectMapper.readTree(response.getContentAsString());
      JsonNode contentNodes = rootNode.path("content");

      List<Long> ids = new ArrayList<>();

      for (JsonNode node : contentNodes) {
        ids.add(node.path("commentId").asLong());
      }

      return ids;
    }


  }

  @Nested
  class 댓글삭제_테스트 {
    private Comment comment;
    private String commentAuthToken;

    private Comment childComment;
    private String childAuthToken;

    @BeforeEach
    public void setup() {
      // 부모 댓글 세팅
      comment = createActiveComment("부모댓글", null);
      commentRepository.save(comment);

      commentAuthToken = simpleEncrypt.encrypt(dummyPassword);
      inMemoryDBProvider.setTemperarily(comment.getCommentId() + "", commentAuthToken, (long) 30);

      // 대댓글 세팅
      childComment = createActiveComment("자식댓글", comment);
      commentRepository.save(childComment);

      childAuthToken = simpleEncrypt.encrypt(dummyPassword);
      inMemoryDBProvider.setTemperarily(childComment.getCommentId() + "", childAuthToken, (long) 30);
    }

    @AfterEach
    public void cleanup() {
      commentRepository.deleteAll();
      commentAuthToken = "";
      childAuthToken = "";
    }

    @Test
    public void 요청이_유효한_경우_상태가_변경된_댓글과_200_응답() throws Exception {

      mockMvc.perform(delete( baseUrl + "/" + comment.getCommentId())
                      .header("Authorization", "Bearer " + commentAuthToken))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.commentId", Matchers.equalTo((int) comment.getCommentId())))
              .andExpect(jsonPath("$.content").exists())
              .andExpect(jsonPath("$.creator").exists())
              .andExpect(jsonPath("$.createdAt").exists())
              .andExpect(jsonPath("$.childrenCommentCount").exists())
              .andExpect(jsonPath("$.postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.status", Matchers.equalTo("inactive")))
              .andExpect(jsonPath("$.password").doesNotExist());

      Comment deletedComment = commentRepository.findByIdWithException(comment.getCommentId());
      assertEquals("inactive", deletedComment.getCommentStatus().getCommentStatus());
    }

    @Test
    public void 부모댓글이_삭제되어도_자식댓글은_삭제되지_않는다() throws Exception {
      mockMvc.perform(delete( baseUrl + "/" + comment.getCommentId())
                      .header("Authorization", "Bearer " + commentAuthToken))
              .andExpect(status().isOk());

      Comment reply = commentRepository.findByIdWithException(childComment.getCommentId());
      assertEquals("active", reply.getCommentStatus().getCommentStatus());

    }

    @Test
    public void 요청에_인증_토큰이_없는_경우_403_응답() throws Exception {

      mockMvc.perform(delete(baseUrl + "/" + comment.getCommentId()))
              .andExpect(status().isForbidden());
    }
  }


  private Comment createActiveComment(String content, Comment parent) {
    return Comment.builder()
            .post(post)
            .content(content)
            .password(simpleEncrypt.encrypt(dummyPassword))
            .creator("me")
            .commentStatus(activeStatus)
            .parentComment(parent)
            .build();
  }

  private Comment createInactiveComment(String content) {
    return Comment.builder()
            .post(post)
            .content(content)
            .password(simpleEncrypt.encrypt(dummyPassword))
            .creator("me")
            .commentStatus(inactiveStatus)
            .build();
  }

  private CommentStatus createCommentStatus(CommentStatusKey status) {
    return CommentStatus.builder()
            .status(status)
            .build();
  }

  private Post createPost(String title) {
    return Post.builder()
            .title(title)
            .textContent("content")
            .category(PostCategory.AD)
            .password(simpleEncrypt.encrypt(dummyPassword))
            .creator("me")
            .build();
  }
}
