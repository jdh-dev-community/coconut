package com.coconut.user_service.domain.user.controller;


import com.coconut.user_service.domain.user.dto.UserCreateDto;
import com.coconut.user_service.domain.user.service.interfaces.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

  private final String baseUrl = "/api/v1/user";

  @MockBean
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private Map<String, String> defaultRequest;

  @BeforeEach
  public void setup() {
    this.defaultRequest = Map.of(
            "signinType", "email",
            "userId", "test@coco",
            "password", "qwer1234#!",
            "mobile", "010-1111-1111",
            "interest", "backend"
    );
  }

  @Nested
  class 회원가입_테스트 {
    private UserCreateDto userCreateDto;
    private String targetUrl = baseUrl + "/signin";

    @Test
    public void 적절한_회원가입_데이터가_제공된_경우_200_응답_반환() throws Exception {
      String validBody = objectMapper.writeValueAsString(defaultRequest);

      postAndVerify(validBody)
              .andExpect(status().isOk());
    }

    @Test
    public void 부적절한_회원가입타입이_입력된_경우_400_응답_반환() throws Exception {
      String invalidData = "random";
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.put("signinType", invalidData);
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody)
              .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입타입이_누락된_경우_400_응답_반환() throws Exception {
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.remove("signinType");
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody)
              .andExpect(status().isBadRequest());
    }


    private ResultActions postAndVerify(String body) throws Exception {
      return mockMvc.perform(post(targetUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
      );
    }
  }

}
