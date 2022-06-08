package com.example.notice.domain.profile.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.example.notice.exception.EmailNotMatchException;

class EmailTest {

	@ParameterizedTest
	@CsvSource({
		"seunghan@naver.com",
		"han@gmail.com",
		"hani@pusan.ac.kr"
	})
	void 패턴_테스트(final String email) {
		Assertions.assertTrue(Pattern.matches(Email.EMAIL_REGEX, email));
	}

	@ParameterizedTest
	@NullAndEmptySource
	void 이메일은_공백이면_안된다(final String nullAndEmpty) {
		assertThrows(IllegalArgumentException.class, () -> new Email(nullAndEmpty));
	}

	@ParameterizedTest
	@CsvSource({
		"seunghan@naver.com",
		"han@gmail.com",
		"hani@pusan.ac.kr"
	})
	void 이메일은_형식에_맞춰_생성_가능하다(final String email) {
		assertDoesNotThrow(() -> new Email(email));
	}

	@ParameterizedTest
	@CsvSource(
		{
			"seunghan",
			"seunghan@",
			"seunghan@naver",
			"seunghan.com"
		}
	)
	void 이메일_형식에_맞추지_못하면_에러를_발생한다(final String email) {
		assertThrows(EmailNotMatchException.class, () -> new Email(email));
	}
}
