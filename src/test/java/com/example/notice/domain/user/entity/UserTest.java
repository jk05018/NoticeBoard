package com.example.notice.domain.user.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UserTest {

	@ParameterizedTest
	@CsvSource({
		"황승한,password123",
		"username,pass",
		"us,p"
	})
	void 이름과_비밀번호로_유저를_생성할수_있다(final Username username, final Password password) {
		// when
		final User user = User.createOf(username, password);

		// then
		assertThat(user.getUsername()).isEqualTo(username);
		assertThat(user.getPassword()).isEqualTo(password);
	}

	@Test
	void 비밀번호를_수정할수_있다() {
		// given
		User user = User.createOf(new Username("황승한"), new Password("password123"));

		// when
		final Password updatePassword = new Password("updatePassword");
		final User updateUser = User.updateOf(updatePassword);

		user.updatePassword(updateUser);

		//then
		assertThat(user.getPassword()).isEqualTo(updatePassword);
	}
}
