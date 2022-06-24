package com.example.notice.exception;

public class NoticeProjectException extends RuntimeException {


  public NoticeProjectException() {
    super();
  }

  public NoticeProjectException(String message) {
    super(message);
  }

  public NoticeProjectException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoticeProjectException(Throwable cause) {
    super(cause);
  }

  protected NoticeProjectException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
