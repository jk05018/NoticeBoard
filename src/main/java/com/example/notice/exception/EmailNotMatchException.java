package com.example.notice.exception;

public class EmailNotMatchException extends RuntimeException{

	public EmailNotMatchException() {
		super("이메일이 일치하지 않습니다.");
	}
}
