package com.example.notice.exception;

public class EmailDuplicatedException extends RuntimeException {

  private static final String MESSAGE = "이미 사용 중인 이메일 입니다.";

  public EmailDuplicatedException() {
    super(MESSAGE);
  }

  public EmailDuplicatedException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
