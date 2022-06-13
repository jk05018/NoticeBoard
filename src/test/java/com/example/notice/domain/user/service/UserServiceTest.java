package com.example.notice.domain.user.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.notice.domain.BasicServiceTest;
import com.example.notice.domain.user.entity.Age;
import com.example.notice.domain.user.entity.Email;
import com.example.notice.domain.user.entity.NickName;
import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.Profile;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.example.notice.domain.user.repository.UserRepository;
import com.example.notice.exception.NoticeProjectException;

class UserServiceTest extends BasicServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void 유저를_이름으로_조회할수_있다() {
		// given
		final Username username = new Username("seunghan");
		final Email email = new Email("email@naver.com");
		final Password password = new Password("password123");

		유저_등록(username, email, password);
		// when
		final User findUser = userService.findByUsername(username);

		// then
		assertThat(findUser.getUsername()).isEqualTo(username);
		assertThat(findUser.getPassword()).isNotNull();
	}

	@Test
	void 아이디와_비밀번호로_로그인할수_있다() {
		// given
		final Username username = new Username("seunghan");
		final Email email = new Email("email@naver.com");
		final String pass = "password123";
		final Password password = new Password(pass);

		유저_등록(username, email, password);

		// when
		final User loginUser = userService.login(username, new Password(pass));

		assertThat(loginUser).isNotNull();
	}

	@Test
	void 프로필을_등록할수_있다() {
		// given
		final Username username = new Username("seunghan");
		유저_등록(username, new Email("email@naber.com"), new Password("pass123"));

		// when
		final NickName nickName = new NickName("nickname");
		final Age age = new Age(25);

		final Profile profile = Profile.of(nickName, age);
		userService.makeProfile(username, profile);

		//then
		final User findUser = userService.findByUsername(username);

		Assertions.assertThat(findUser.getProfile()).isEqualTo(profile);
		Assertions.assertThat(findUser.getProfile().getNickName()).isEqualTo(nickName);
		Assertions.assertThat(findUser.getProfile().getAge()).isEqualTo(age);
	}

	public void 유저_등록(final Username username, final Email email, final Password password) {

		try {
			final User user = User.createOf(username, email, password);

			userService.registerUser(user);
		} catch (Exception e) {
			throw new NoticeProjectException("UserServiceTest 유저 등록 실패", e);
		}

	}

}
