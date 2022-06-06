package com.example.notice.domain.profile.service;

import com.example.notice.domain.profile.entity.Age;
import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;

public interface ProfileService {

	Profile createProfile(final NickName nickName, final Age age);

	Profile getProfileById(final Long id);

	Profile getProfileByNickName(final NickName nickName);

	void deleteProfile(final Long id);

}
