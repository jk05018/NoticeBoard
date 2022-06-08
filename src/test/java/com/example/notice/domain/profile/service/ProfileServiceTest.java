package com.example.notice.domain.profile.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.notice.domain.BasicServiceTest;
import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.Email;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.repository.ProfileRepository;
import com.example.notice.exception.EmailDuplicatedException;
import com.example.notice.exception.NoSuchProfileException;

class ProfileServiceTest extends BasicServiceTest {

	@Autowired
	ProfileService profileService;

	@Autowired
	ProfileRepository profileRepository;

	@Test
	void 프로필의_이메일은_중복_등록될_수_없다() {
		final Profile duplcatedEmailProfile = Profile.of(new NickName("new nickname"), profile.getEmail(), new Age(23));

		assertThrows(EmailDuplicatedException.class, () -> profileService.createProfile(duplcatedEmailProfile));
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

		assertThrows(NoSuchProfileException.class, () -> profileService.getProfileByNickName(profile.getNickName()));
	}


}
