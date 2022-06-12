package com.example.notice.exception;

public class NoSuchUserException extends RuntimeException{

	public NoSuchUserException() {
		super("해당하는 프로필이 존재하지 않습니다.");
	}

	public NoSuchUserException(String message) {
		super(message);
	}
}
