package com.example.notice.util;

import javax.servlet.http.HttpServletRequest;


public final class Tokens {
	public static final String TOKEN_HEADER = "token";

	private Tokens(){}

	public static String get(final HttpServletRequest request){
		return request.getHeader(TOKEN_HEADER);
	}

}
