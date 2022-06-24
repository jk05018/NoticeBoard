package com.example.notice.exception;

public class NoSuchUserException extends RuntimeException {

  private static final String MESSAGE = "해당하는 프로필이 존재하지 않습니다.";

  public NoSuchUserException() {
    super(MESSAGE);
  }

  public NoSuchUserException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
