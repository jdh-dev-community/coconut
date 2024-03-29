package com.coconut.quiz_spring.domain.quiz.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizDto {
  private String quiz;
  private String answer;
  private List<String> keywords;

  @JsonCreator
  public static QuizDto of(String quiz, String answer, String keywords) {
    List<String> formatted = Arrays.asList(keywords.split(","));
    return new QuizDto(quiz, answer, formatted);
  }

  @Override
  public String toString() {
    return "QuizDto{" +
            "quiz='" + quiz + '\'' +
            ", answer='" + answer + '\'' +
            ", keywords='" + keywords + '\'' +
            '}';
  }
}
