package com.coconut.global.advice;

import com.coconut.global.dto.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

@Slf4j
public class BaseControllerAdvice {

  protected HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest req, Exception ex) {
    final String path = req.getDescription(false).replace("uri=", "");
    final String message = ex.getMessage();
    final String errorName = ex.getClass().getName();

    log.debug("Returning HTTP status: {} for path: {}, message: {}, error name: {}", httpStatus, path, message, errorName);
    return new HttpErrorInfo(httpStatus, path, message, errorName);
  }

}
