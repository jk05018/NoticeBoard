package com.example.notice.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
	protected Profile profile2;

	@BeforeEach
	void setUp() {
		final NickName nickName = new NickName("profile");
		final Email email = new Email("profile@naver.com");
		final Age age = new Age(25);

		profile = 프로필_생성(nickName, email ,age);
		final NickName nickName2 = new NickName("profile2");
		final Email email2 = new Email("profile2@naver.com");
		final Age age2 = new Age(30);

		profile2 = 프로필_생성(nickName2, email2 ,age2);


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
