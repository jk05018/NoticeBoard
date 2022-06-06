package com.example.notice.domain.profile.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.notice.domain.AbstractServiceTest;
import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.repository.ProfileRepository;

class ProfileServiceTest extends AbstractServiceTest {

	@Autowired
	ProfileService profileService;

	@Autowired
	ProfileRepository profileRepository;

	// JPA의 기본적인 CURD 기능은 테스트하지 않는다! 커스터마이징 한 메서드를 테스트 하도록 하자.

	@Test
	void 프로필_정보를_가져올_수_있는가() {
		// given
		final NickName nickname = new NickName("seunghan");
		final Age age = new Age(35);
		final Profile profile = Profile.of(nickname, age);

		// given(profileRepository.findProfileByNickName(nickname)).willReturn(Optional.ofNullable(profile));

		profileService.createProfile(nickname, age);

		// when
		final Profile findProfile = profileService.getProfileByNickName(nickname);

		// then
		assertThat(findProfile).isNotNull();

	}
}
