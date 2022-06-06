package com.example.notice.domain.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notice.domain.profile.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
