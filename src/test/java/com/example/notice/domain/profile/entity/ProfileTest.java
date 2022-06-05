package com.example.notice.domain.profile.entity;

import static org.assertj.core.api.Assertions.*;

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
		assertThat(profile.getNickName()).isEqualTo(nickname);
		assertThat(profile.getAge()).isEqualTo(age);
	}

}
