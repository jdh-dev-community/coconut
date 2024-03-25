package com.coconut.quiz_spring.common.advice;

import com.coconut.quiz_spring.common.dto.CustomResponse;
import com.coconut.quiz_spring.common.dto.HttpErrorInfo;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public @ResponseBody CustomResponse handleMethodArgumentNotValidException(WebRequest req, MethodArgumentNotValidException ex) {
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public @ResponseBody CustomResponse handleIllegalArgumentException(WebRequest req, IllegalArgumentException ex) {
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public @ResponseBody CustomResponse handleDataIntegrityViolationException(WebRequest req, DataIntegrityViolationException ex) {
    HttpErrorInfo errorInfo;
    Throwable rootCause = ex.getRootCause();

    if (rootCause instanceof MysqlDataTruncation) {
      errorInfo = createHttpErrorInfo(BAD_REQUEST, req,  (MysqlDataTruncation) rootCause);
    } else {
      errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    }

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
