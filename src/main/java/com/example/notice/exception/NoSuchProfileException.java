package com.example.notice.exception;

public class NoSuchProfileException extends RuntimeException {

	public NoSuchProfileException() {
		super("해당하는 프로필이 존재하지 않습니다.");
	}

	public NoSuchProfileException(String message) {
		super(message);
	}
}
