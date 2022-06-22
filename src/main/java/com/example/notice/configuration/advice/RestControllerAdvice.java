package com.example.notice.configuration.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import com.example.notice.exception.NoSuchUserException;
import com.example.notice.exception.NoticeProjectException;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@org.springframework.web.bind.annotation.RestControllerAdvice
public class RestControllerAdvice {

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({RuntimeException.class, Exception.class})
  public ErrorResponse handleBadRequest(final Throwable cause) {
    log.error(cause.getMessage(), cause);
    return ErrorResponse.of();
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler(NoticeProjectException.class)
  public ErrorResponse handleBadRequest(final NoticeProjectException e) {
    log.error(e.getMessage(), e);
    return ErrorResponse.of(e);
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(NoSuchUserException.class)
  public ErrorResponse handleBadRequest(final NoSuchUserException e) {
    log.error(e.getMessage(), e);
    return ErrorResponse.of(e);
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler(AuthenticationException.class)
  public ErrorResponse handleBadRequest(final AuthenticationException e) {
    log.error(e.getMessage(), e);
    return ErrorResponse.of(e);
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ExceptionHandler(BeansException.class)
  public ResponseEntity<ErrorResponse> handleUnprocessableEntity(final BeansException e) {
    log.error(e.getMessage(), e);
    return ResponseEntity.unprocessableEntity()
        .body(ErrorResponse.of("유효하지 않은 값이 있습니다."));
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleUnprocessableEntity(final IllegalArgumentException e) {
    log.error(e.getMessage(), e);
    return ResponseEntity.unprocessableEntity()
        .body(ErrorResponse.of(e));
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleUnprocessableEntity(final IllegalStateException e) {
    log.error(e.getMessage(), e);
    return ResponseEntity.unprocessableEntity()
        .body(ErrorResponse.of(e));
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleUnprocessableEntity(
      final ConstraintViolationException e) {
    log.error(e.getMessage(), e);
    return ResponseEntity.unprocessableEntity()
        .body(ErrorResponse.of(e));
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ExceptionHandler(ServletRequestBindingException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(final ServletRequestBindingException e) {
    log.error(e.getMessage(), e);
    return ResponseEntity.unprocessableEntity()
        .body(ErrorResponse.of(e));
  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ErrorResponse {

    @JsonProperty("errors")
    private final Error error;

    public static ErrorResponse of() {
      return new ErrorResponse(Error.of());
    }

    public static ErrorResponse of(final String message) {
      return new ErrorResponse(Error.of(message));
    }

    public static ErrorResponse of(final RuntimeException e) {
      return new ErrorResponse(Error.of(e));
    }

    public static ErrorResponse of(final ServletRequestBindingException e) {
      return new ErrorResponse(Error.of(e));
    }

    public static ErrorResponse of(final ConstraintViolationException e) {
      return new ErrorResponse(Error.of(e));
    }

  }

  @Getter
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Error {

    public static final Error EMPTY = new Error(Collections.emptyList());

    public static final String DEFAULT_MESSAGE = "일시적으로 서비스가 지연되고 있습니다. 잠시 후에 다시 이용해 주시기 바랍니다.";

    @JsonProperty("body")
    private final List<String> bodies;

    public static Error of() {
      return new Error(List.of(DEFAULT_MESSAGE));
    }

    public static Error of(final String message) {
      return new Error(List.of(message));
    }

    public static Error of(final RuntimeException e) {
      return new Error(List.of(Optional.ofNullable(e.getMessage()).orElse(DEFAULT_MESSAGE)));
    }

    public static Error of(final ServletRequestBindingException e) {
      return new Error(List.of(Optional.ofNullable(e.getMessage()).orElse(DEFAULT_MESSAGE)));
    }

    public static Error of(final ConstraintViolationException e) {
      if (CollectionUtils.isEmpty(e.getConstraintViolations())) {
        return EMPTY;
      }

      return new Error(e.getConstraintViolations()
          .stream()
          .map(ConstraintViolation::getMessage)
          .collect(Collectors.toUnmodifiableList()));
    }
  }

}
