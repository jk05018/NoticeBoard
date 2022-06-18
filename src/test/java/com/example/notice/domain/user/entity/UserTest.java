package com.example.notice.domain.user.entity;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UserTest {

	@ParameterizedTest
	@CsvSource({
		"황승한,email@naver.com,password123",
		"username,seunghan@gmail.com,pass",
		"us,a@na.com,p"
	})
	void 이름과_비밀번호로_유저를_생성할수_있다(final Username username, final Email email, final Password password) {
		// when
		final User user = User.createOf(username, email, password);

		// then
		assertThat(user).extracting(User::getUsername, User::getEmail, User::getPassword)
			.isEqualTo(List.of(username, email, password));
	}

	@Test
	void 비밀번호를_수정할수_있다() {
		// given
		User user = User.createOf(new Username("황승한"),new Email("seunghan@gmail.com"), new Password("password123"));

		// when
		final Email updateEmail = new Email("updateemail@email.com");
		final Password updatePassword = new Password("updatePassword");

		final User updateUser = User.updateOf(updateEmail, updatePassword);

		user.updateUser(updateUser);

		//then
		assertThat(user).extracting(User::getEmail, User::getPassword)
			.isEqualTo(List.of(updateEmail, updatePassword));
	}
}
