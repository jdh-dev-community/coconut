package com.coconut.global.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomObjectMapper {

  private final ObjectMapper objectMapper;

  public CustomObjectMapper() {
    this.objectMapper = new ObjectMapper();
  }

  public String writeValueAsString(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException("safeWriteValueAsString에서 serialize 중 에러가 발생했습니다. dto: " + value);
    }
  }

}
