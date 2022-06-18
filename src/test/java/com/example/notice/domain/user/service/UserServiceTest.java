package com.example.notice.domain.user.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

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
		final Username username = new Username("test");
		final Email email = new Email("test@naver.com");
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
		final Username username = new Username("test");
		final Email email = new Email("test@naver.com");
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
		final Username username = new Username("test");
		유저_등록(username, new Email("test@naber.com"), new Password("pass123"));

		// when
		final NickName nickName = new NickName("testNickName");
		final Age age = new Age(25);

		final Profile profile = Profile.of(nickName, age);
		userService.makeProfile(username, profile);

		//then
		final User findUser = userService.findByUsername(username);

		assertThat(findUser.getProfile()).isEqualTo(profile);
		assertThat(findUser.getProfile()).extracting(Profile::getNickName, Profile::getAge)
			.isEqualTo(List.of(nickName, age));
	}

}
