package com.example.notice.domain.profile.service;

import org.springframework.stereotype.Service;

import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.repository.ProfileRepository;
import com.example.notice.exception.NoSuchProfileException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;

	@Override
	public Profile createProfile(NickName nickName, Age age) {
		return profileRepository.save(Profile.of(nickName, age));
	}

	@Override
	public Profile getProfileById(Long id) {
		return profileRepository.findById(id)
			.orElseThrow(() -> new NoSuchProfileException());
	}

	@Override
	public Profile getProfileByNickName(NickName nickName) {
		return profileRepository.findProfileByNickName(nickName)
			.orElseThrow(() -> new NoSuchProfileException());
	}

	@Override
	public void deleteProfile(Long id) {
		profileRepository.deleteById(id);
	}

}
