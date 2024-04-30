package com.coconut.jpa_utils.advice;

import com.coconut.global.advice.BaseControllerAdvice;
import com.coconut.global.constant.ControllerAdviceOrder;
import com.coconut.global.dto.CustomResponse;
import com.coconut.global.dto.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Slf4j
@Order(ControllerAdviceOrder.SHARE)
@ControllerAdvice
public class JPAExceptionAdvice extends BaseControllerAdvice {
  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(OptimisticLockException.class)
  public @ResponseBody CustomResponse handleOptimisticLockException(WebRequest req, OptimisticLockException ex) {
    log.info("error handler: handleOptimisticLockException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(BAD_REQUEST, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(JpaObjectRetrievalFailureException.class)
  public @ResponseBody CustomResponse handleJpaObjectRetrievalFailureException(WebRequest req, JpaObjectRetrievalFailureException ex) {
    log.info("error handler: handleJpaObjectRetrievalFailureException");
    HttpErrorInfo errorInfo = createHttpErrorInfo(NOT_FOUND, req, ex);
    return CustomResponse.of(errorInfo);
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  public @ResponseBody CustomResponse handleInvalidDataAccessApiUsageException(WebRequest req, InvalidDataAccessApiUsageException ex) {
    log.info("error handler: handleInvalidDataAccessApiUsageException");
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
}
