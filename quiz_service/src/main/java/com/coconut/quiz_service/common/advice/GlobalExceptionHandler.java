package com.coconut.quiz_service.common.advice;

import com.coconut.quiz_service.common.dto.CustomResponse;
import com.coconut.quiz_service.common.dto.HttpErrorInfo;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  public @ResponseBody CustomResponse handleInvalidDataAccessApiUsageException(WebRequest req, InvalidDataAccessApiUsageException ex) {
    log.info("error handler: handleInvalidDataAccessApiUsageException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public @ResponseBody CustomResponse handleBindException(WebRequest req, BindException ex) {
    log.info("error handler: handleBindException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }
  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public @ResponseBody CustomResponse handleEntityNotFoundException(WebRequest req, EntityNotFoundException ex) {
    log.info("error handler: handleEntityNotFoundException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public @ResponseBody CustomResponse handleMethodArgumentNotValidException(WebRequest req, MethodArgumentNotValidException ex) {
    log.info("error handler: handleMethodArgumentNotValidException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public @ResponseBody CustomResponse handleIllegalArgumentException(WebRequest req, IllegalArgumentException ex) {
    log.info("error handler: handleIllegalArgumentException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public @ResponseBody CustomResponse handleDataIntegrityViolationException(WebRequest req, DataIntegrityViolationException ex) {
    log.info("error handler: handleDataIntegrityViolationException");
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
