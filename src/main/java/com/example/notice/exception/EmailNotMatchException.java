package com.example.notice.exception;

public class EmailNotMatchException extends RuntimeException {

  private static final String MESSAGE = "이메일이 일치하지 않습니다.";

  public EmailNotMatchException() {
    super(MESSAGE);
  }

  public EmailNotMatchException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
