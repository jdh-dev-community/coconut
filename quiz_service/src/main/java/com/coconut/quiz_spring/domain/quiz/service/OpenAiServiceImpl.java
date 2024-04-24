package com.coconut.quiz_spring.domain.quiz.service;

import com.coconut.quiz_spring.common.config.OpenAiProperties;
import com.coconut.quiz_spring.domain.quiz.dto.AnswerDto;
import com.coconut.quiz_spring.domain.quiz.dto.QuizDto;
import com.coconut.quiz_spring.domain.quiz.exception.ExceedOpenAiQuotaException;
import com.coconut.quiz_spring.domain.quiz.exception.InvalidOpenAiKeyException;
import com.coconut.quiz_spring.domain.quiz.service.interfaces.OpenAiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements OpenAiService {

  private final RestTemplate restTemplate;

  private final OpenAiProperties openAiProperties;

  private final ObjectMapper objectMapper;

  public QuizDto generateQuiz() {
    String url = openAiProperties.getApi().getUrl();
    String guide = openAiProperties.getQuiz().getGuide();
    String prompt = openAiProperties.getQuiz().getPrompt();

    HttpEntity<String> entity = createHttpEntity(guide, prompt);
    ResponseEntity<String> response = request(url, entity, HttpMethod.POST);

    return getContent(response.getBody(), QuizDto.class);
  }

  public AnswerDto generateAnswer(long quizId, String quiz, String keyword, String answer) {
    String url = openAiProperties.getApi().getUrl();
    String baseGuide = openAiProperties.getAnswer().getGuide();
    String prefixGuide = "문제: " + quiz + ". " + "핵심 키워드: " + keyword;

    String prefixAnswer = "저의 정답입니다. 채점해주세요. 정답: ";

    HttpEntity<String> entity = createHttpEntity(prefixGuide + baseGuide, prefixAnswer + answer);
    ResponseEntity<String> response = request(url, entity, HttpMethod.POST);

    Map<String, Object> result = getContent(response.getBody(), Map.class);
    return AnswerDto.of(
            quizId,
            (int) result.get("score"),
            (String) result.get("reason"),
            (String) result.get("feedback"),
            keyword);
  }

  private ResponseEntity<String> request(String url, HttpEntity<String> entity, HttpMethod method) {

    try {
      log.info("openai request started: >>");
      ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
      log.info("openai request finished: >> " + response);
      return response;
    } catch (HttpClientErrorException.Unauthorized ex) {
      throw new InvalidOpenAiKeyException(ex.getMessage(), ex.getStatusCode(), ex.getCause());
    } catch (HttpClientErrorException.TooManyRequests ex) {
      throw new ExceedOpenAiQuotaException(ex.getMessage(), ex.getStatusCode(), ex.getCause());
    } catch (HttpClientErrorException.BadRequest ex) {
      log.error("$$ bad request url: >> {}", url);
      log.error("$$ bad request method: >> {}", method);
      log.error("$$ bad request entity: >> {}", entity);
      log.error("$$ bad request entity: >> {}", entity.getBody());
      throw ex;
    }
  }


  private HttpEntity<String> createHttpEntity(String guide, String prompt) {
    String key = openAiProperties.getApi().getKey();
    String model = openAiProperties.getApi().getModel();

    String requestBody = createBody(model, guide, prompt);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + key);

    return new HttpEntity<>(requestBody, headers);
  }

//          바디 모양
//            {
//              "model": model,
//              "messages": [
//                { "role": "system", "content": guide },
//                { "role": "user", "content": prompt }
//              ]
//            }
  private String createBody(String model, String guide, String prompt) {

    try {
      // 메시지 리스트 생성
      Map<String, String> systemMessage = Map.of("role", "system", "content", guide);
      Map<String, String> userMessage = Map.of("role", "user", "content", prompt);
      List<Map<String, String>> messages = List.of(systemMessage, userMessage);
      Map<String, Object> jsonMap = Map.of("model", model, "messages", messages);

      return objectMapper.writeValueAsString(jsonMap);
    } catch (JsonProcessingException ex) {
      log.error("$$ model: >> {}", model);
      log.error("$$ guide: >> {}", guide);
      log.error("$$ prompt: >> {}", prompt);
      throw new IllegalArgumentException();
    }
  }



  private <T> T getContent(String responseBody, Class<T> dtoClass) {
    try {
      JsonNode rootNode = objectMapper.readTree(responseBody);
      JsonNode choicesNode = rootNode.path("choices");

      JsonNode firstChoice = choicesNode.get(0);
      JsonNode messageNode = firstChoice.path("message");
      String contentJson = messageNode.path("content").asText();

      T content = objectMapper.readValue(contentJson, dtoClass);
      return content;
    } catch (JsonProcessingException ex) {
      throw new RuntimeException("openai 응답 파싱에 실패하였습니다." + ex.getMessage());
    }

  }

}
