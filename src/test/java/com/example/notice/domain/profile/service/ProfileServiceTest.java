package com.example.notice.domain.profile.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.notice.domain.AbstractServiceTest;
import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.repository.ProfileRepository;
import com.example.notice.exception.NoSuchProfileException;

class ProfileServiceTest extends AbstractServiceTest {

	@Autowired
	ProfileService profileService;

	@Autowired
	ProfileRepository profileRepository;

	private Profile profile;

	@BeforeEach
	void setUp() {
		final NickName nickName = new NickName("seunghan");
		final Age age = new Age(25);

		profile = 프로필_생성(nickName, age);
	}

	@Test
	void 프로필_정보를_가져올_수_있는가() {
		// given, when
		final Profile findProfile = profileService.getProfileByNickName(profile.getNickName());

		// then
		assertThat(findProfile).isEqualTo(profile);
	}

	@Test
	void 프로필을_삭제할_수_있다() {
		profileService.deleteProfileByNickname(profile.getNickName());

		// assertThrows(NoSuchProfileException.class , () -> profileRepository.findProfileByNickName(profile.getNickName()));
		assertThrows(NoSuchProfileException.class, () -> profileService.getProfileByNickName(profile.getNickName()));
	}

	private Profile 프로필_생성(final NickName nickName, final Age age) {
		final Profile profile = Profile.of(nickName, age);
		final Profile savedProfile = profileService.createProfile(profile);

		assertThat(savedProfile).isNotNull();

		return savedProfile;

	}
}
