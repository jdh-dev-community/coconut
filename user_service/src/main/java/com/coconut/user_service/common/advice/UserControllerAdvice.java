package com.coconut.user_service.common.advice;

import com.coconut.global.advice.BaseControllerAdvice;
import com.coconut.global.constant.ControllerAdviceOrder;
import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.UnexpectedTypeException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Order(ControllerAdviceOrder.SERVICE)
@ControllerAdvice
public class UserControllerAdvice extends BaseControllerAdvice {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(UnexpectedTypeException.class)
  public @ResponseBody CustomResponse handleUnexpectedTypeException(WebRequest req, Exception ex) {
    log.info("error handler: handleUnexpectedTypeException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }

}
