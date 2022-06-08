package com.example.notice.exception;

public class NoSuchNoticeException extends RuntimeException{

	public NoSuchNoticeException() {
		super("해당 공지사항이 존재하지 않습니다.");
	}
}
