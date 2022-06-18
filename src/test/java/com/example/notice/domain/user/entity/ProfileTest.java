package com.example.notice.domain.user.entity;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("프로필 테스트")
class ProfileTest {

	@Test
	void Profile_생성_테스트() {
		// given
		final NickName nickname = new NickName("han");
		final Age age = new Age(20);

		// when
		final Profile profile = Profile.of(nickname, age);

		// then
		assertThat(profile).extracting(Profile::getNickName, Profile::getAge)
			.isEqualTo(List.of(nickname, age));
	}

}
