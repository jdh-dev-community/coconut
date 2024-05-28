package com.coconut.auth_service.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;


@Slf4j
public class CustomContentCachingRequestWrapper extends ContentCachingRequestWrapper {
  private final ObjectMapper objectMapper;

  public CustomContentCachingRequestWrapper(HttpServletRequest request, ObjectMapper objectMapper) {
    super(request);
    this.objectMapper = objectMapper;
  }

  public <T> T getBody(Class<T> className) throws IOException {
    byte[] content = super.getContentAsByteArray();

    if (content.length == 0) {
      return objectMapper.readValue(super.getInputStream(), className);
    }  else {
      return objectMapper.readValue(content, className);
    }
  }
}
