package com.example.notice.exception;

public class EmailDuplicatedException extends RuntimeException{

	public EmailDuplicatedException() {
		super("이미 사용중인 이메일입니다.");
	}
}
