package com.example.notice.exception;

public class NoSuchProfileException extends RuntimeException {

  private static final String MESSAGE = "해당하는 프로필이 존재하지 않습니다.";

  public NoSuchProfileException() {
    super(MESSAGE);
  }

  public NoSuchProfileException(Throwable cause) {
    super(MESSAGE);
  }
}
