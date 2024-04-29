package com.coconut.user_service.common.advice;

import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.PriorityOrdered;
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
@Order(PriorityOrdered.LOWEST_PRECEDENCE - 1)
@ControllerAdvice
public class UserControllerAdvice {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(UnexpectedTypeException.class)
  public @ResponseBody CustomResponse handleUnexpectedTypeException(WebRequest req, Exception ex) {
    log.info("error handler: handleUnexpectedTypeException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
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
