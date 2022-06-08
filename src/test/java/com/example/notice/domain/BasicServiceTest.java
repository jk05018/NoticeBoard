package com.example.notice.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.Email;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.service.ProfileService;
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
	private ProfileService profileService;

	protected Profile profile;

	@BeforeEach
	void setUp() {
		final NickName nickName = new NickName("seunghan");
		final Email email = new Email("seunghan@naver.com");
		final Age age = new Age(25);

		profile = 프로필_생성(nickName, email ,age);
	}

	protected Profile 프로필_생성(final NickName nickName, final Email email, final Age age) {
		try {
			final Profile profile = Profile.of(nickName, email, age);
			final Profile savedProfile = profileService.createProfile(profile);

			assertThat(savedProfile).isNotNull();

			return savedProfile;
		} catch (Exception e) {
			throw new NoticeProjectException();
		}
	}
}
