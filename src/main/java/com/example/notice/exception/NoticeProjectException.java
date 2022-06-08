package com.example.notice.exception;

public class NoticeProjectException extends RuntimeException {

	public NoticeProjectException() {
		super("notice project exception 입니다.");
	}

	public NoticeProjectException(String message) {
		super(message);
	}
}
