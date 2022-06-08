package com.example.notice.domain.profile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.repository.ProfileRepository;
import com.example.notice.exception.EmailDuplicatedException;
import com.example.notice.exception.NoSuchProfileException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final ProfileRepository profileRepository;

	@Override
	public Profile createProfile(final Profile profile) {
		if(profileRepository.existsProfileByEmail(profile.getEmail())){
			throw new EmailDuplicatedException();
		}

		return profileRepository.save(profile);
	}

	@Override
	public Profile getProfileById(final Long id) {
		return profileRepository.findById(id)
			.orElseThrow(() -> new NoSuchProfileException());
	}

	@Override
	public Profile getProfileByNickName(final NickName nickName) {
		return profileRepository.findProfileByNickName(nickName)
			.orElseThrow(() -> new NoSuchProfileException());
	}

	@Override
	public void deleteProfileByNickname(final NickName nickName) {
		final Profile profile = profileRepository.findProfileByNickName(nickName)
			.orElseThrow(() -> new NoSuchProfileException());

		profileRepository.deleteById(profile.getId());

	}

}
