package com.example.notice.domain.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.notice.domain.BasicServiceTest;
import com.example.notice.domain.user.entity.Password;
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
		final Password password = new Password("password123");

		System.out.println(Password.toString(password));

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
		final String pass = "password123";
		final Password password = new Password(pass);

		유저_등록(username, password);

		// when
		final User loginUser = userService.login(username, new Password(pass));

		assertThat(loginUser).isNotNull();
	}

	public void 유저_등록(final Username username, final Password password){

		try{
			final User user = User.createOf(username, password);

			userService.registerUser(user);
		}catch (Exception e){
			throw new NoticeProjectException("UserServiceTest 유저 등록 실패", e);
		}

	}


}
