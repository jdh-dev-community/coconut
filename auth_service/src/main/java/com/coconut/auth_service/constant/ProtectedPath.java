package com.coconut.auth_service.constant;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProtectedPath {
  private final String baseV1 = "/api/v1";

  private final List<String> quizService = List.of("/validation");

  private final List<String> communityService = List.of();


  public String[] getRoute() {
    return Stream.of(quizService, communityService)
            .flatMap(List::stream)
            .map((path) -> baseV1 + path)
            .toArray(String[]::new);
  }

}
