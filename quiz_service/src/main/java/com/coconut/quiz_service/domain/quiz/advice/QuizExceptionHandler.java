package com.coconut.quiz_service.domain.quiz.advice;

import com.coconut.global.advice.BaseControllerAdvice;
import com.coconut.global.constant.ControllerAdviceOrder;
import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.HttpErrorInfo;
import com.coconut.quiz_service.domain.quiz.exception.ExceedOpenAiQuotaException;
import com.coconut.quiz_service.domain.quiz.exception.InvalidOpenAiKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
@Order(ControllerAdviceOrder.SERVICE)
public class QuizExceptionHandler extends BaseControllerAdvice {

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

}
