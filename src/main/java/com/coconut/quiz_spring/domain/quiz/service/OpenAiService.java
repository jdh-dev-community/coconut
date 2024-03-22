package com.coconut.quiz_spring.domain.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {

  private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

  @Autowired
  private ObjectMapper objectMapper;

  @Value("${openai.api.key}")
  private String OPENAI_API_KEY;

  @Value("${openai.api.quiz.guide}")
  private String OPENAI_QUIZ_PROMPT_GUIDE;

  @Value("${openai.api.quiz.prompt}")
  private String OPENAI_QUIZ_PROMPT;

  @Value("${openai.api.answer.guide}")
  private String OPENAI_ANSWER_PROMPT_GUIDE;

  public String generateAnswer(String answer) throws IOException, InterruptedException {
    String requestBody = """
            {
              "model": "gpt-3.5-turbo",
              "messages": [
                { "role": "system", "content": "%s" },
                { "role": "user", "content": "%s" }
              ]
            }""".formatted(OPENAI_ANSWER_PROMPT_GUIDE, answer);

    String body = createHttpRequest(requestBody);
    String content = getContent(body);

    return content;
  }


  public String generateQuiz() throws IOException, InterruptedException {


    String requestBody = """
            {
              "model": "gpt-3.5-turbo",
              "messages": [
                { "role": "system", "content": "%s" },
                { "role": "user", "content": "%s" }
              ]
            }""".formatted(OPENAI_QUIZ_PROMPT_GUIDE, OPENAI_QUIZ_PROMPT);


    String body = createHttpRequest(requestBody);
    String content = getContent(body);

    return content;
  }

  private String createHttpRequest(String requestBody) throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(OPENAI_URL))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + OPENAI_API_KEY)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    return response.body();
  }

  private String getContent(String responseBody) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode = objectMapper.readTree(responseBody);
    JsonNode choicesNode = rootNode.path("choices");

    JsonNode firstChoice = choicesNode.get(0);
    JsonNode messageNode = firstChoice.path("message");
    String content = messageNode.path("content").asText();

    return content;
  }

}
