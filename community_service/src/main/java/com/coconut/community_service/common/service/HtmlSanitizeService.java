package com.coconut.community_service.common.service;

import lombok.RequiredArgsConstructor;
import org.owasp.html.PolicyFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HtmlSanitizeService {
  private final PolicyFactory htmlSanitizer;

  public String sanitize(String rawHtml) {
    return htmlSanitizer.sanitize(rawHtml);
  }
}
