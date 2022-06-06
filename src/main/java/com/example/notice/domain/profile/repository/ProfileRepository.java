package com.example.notice.domain.profile.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.notice.domain.profile.entity.NickName;
import com.example.notice.domain.profile.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	Optional<Profile> findProfileByNickName(final NickName nickName);

	Long deleteProfileByNickName(final NickName nickName);
}
