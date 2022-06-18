package com.example.notice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.notice.domain.user.entity.Age;
import com.example.notice.domain.user.entity.Email;
import com.example.notice.domain.user.entity.NickName;
import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.Profile;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.example.notice.domain.user.service.UserService;
import com.example.notice.exception.NoticeProjectException;

@Transactional
@SpringBootTest
@TestPropertySource(properties = {
	"spring.jpa.properties.hibernate.show_sql=true",
	"spring.jpa.properties.hibernate.format_sql=true",
	"logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
})
public class BasicServiceTest {

	@Autowired
	private UserService userService;

	protected User testUser;
	protected Profile testProfile;

	@BeforeEach
	void setUp() {
		testUser = 유저_등록(new Username("seunghan"), new Email("han@naver.com"), new Password("pass123"));
		testProfile = 프로필_등록(testUser, new NickName("hani"), new Age(25));
	}

	protected User 유저_등록(final Username username, final Email email, final Password password) {
		try {
			final User user = User.createOf(username, email, password);

			userService.registerUser(user);

			return userService.findByUsername(username);
		} catch (Exception e) {
			throw new NoticeProjectException("UserServiceTest 유저 등록 실패", e);
		}
	}

	protected Profile 프로필_등록(final User user, final NickName nickName, final Age age) {
		try {
			final Profile profile = Profile.of(nickName, age);
			userService.makeProfile(user.getUsername(), profile);

			return userService.findByUsername(user.getUsername()).getProfile();
		} catch (Exception e) {
			throw new NoticeProjectException("테스트 서비스 프로필 등록 실패입니다.", e);
		}
	}

}
