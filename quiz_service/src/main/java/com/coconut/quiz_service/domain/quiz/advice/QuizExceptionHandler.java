package com.coconut.quiz_service.domain.quiz.advice;

import com.coconut.quiz_service.common.dto.CustomResponse;
import com.coconut.quiz_service.common.dto.HttpErrorInfo;
import com.coconut.quiz_service.domain.quiz.exception.ExceedOpenAiQuotaException;
import com.coconut.quiz_service.domain.quiz.exception.InvalidOpenAiKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class QuizExceptionHandler {

  @ResponseStatus(TOO_MANY_REQUESTS)
  @ExceptionHandler(ExceedOpenAiQuotaException.class)
  public @ResponseBody CustomResponse handleExceedOpenAiQuotaException(WebRequest req, ExceedOpenAiQuotaException ex) {
    log.info("error handler: handleExceedQuotaException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(TOO_MANY_REQUESTS, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(InvalidOpenAiKeyException.class)
  public @ResponseBody CustomResponse handleInvalidOpenAiKeyException(WebRequest req, InvalidOpenAiKeyException ex) {
    log.info("error handler: handleInvalidApiKeyException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(UNAUTHORIZED, req, ex);
    return CustomResponse.of(errorInfo);
  }

  private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest req, Exception ex) {
    final String path = req.getDescription(false).replace("uri=", "");
    final String message = ex.getMessage();
    final String errorName = ex.getClass().getName();

    log.debug("Returning HTTP status: {} for path: {}, message: {}, error name: {}", httpStatus, path, message, errorName);
    return new HttpErrorInfo(httpStatus, path, message, errorName);
  }
}
