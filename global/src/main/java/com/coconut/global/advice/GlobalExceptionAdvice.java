package com.coconut.global.advice;

import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.HttpErrorInfo;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice extends BaseControllerAdvice {

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public @ResponseBody CustomResponse handleAllException(WebRequest req, Exception ex) {
    log.info("error handler: handleAllException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(INTERNAL_SERVER_ERROR, req, ex);
    return CustomResponse.of(errorInfo);
  }
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public @ResponseBody CustomResponse handleMethodArgumentTypeMismatchException(WebRequest req, MethodArgumentTypeMismatchException ex) {
    log.info("error handler: handleMethodArgumentTypeMismatchException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public @ResponseBody CustomResponse handleHttpMessageNotReadableException(WebRequest req, HttpMessageNotReadableException ex) {
    log.info("error handler: handleHttpMessageNotReadableException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(ValueInstantiationException.class)
  public @ResponseBody CustomResponse handleValueInstantiationExceptionException(WebRequest req, ValueInstantiationException ex) {
    log.info("error handler: handleValueInstantiationExceptionException");
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

}
