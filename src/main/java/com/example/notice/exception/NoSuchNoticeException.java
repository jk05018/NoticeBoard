package com.example.notice.exception;

public class NoSuchNoticeException extends RuntimeException {

  private static final String MESSAGE = "해당 공지사항이 존재하지 않습니다.";

  public NoSuchNoticeException() {
    super(MESSAGE);
  }

  public NoSuchNoticeException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
