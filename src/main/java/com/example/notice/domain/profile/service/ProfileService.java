package com.example.notice.domain.profile.service;

import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;

public interface ProfileService {

	Profile createProfile(final Profile profile);

	Profile getProfileById(final Long id);

	Profile getProfileByNickName(final NickName nickName);

	void deleteProfileByNickname(final NickName nickName);

}
