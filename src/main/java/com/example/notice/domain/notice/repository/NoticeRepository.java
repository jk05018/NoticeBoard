package com.example.notice.domain.notice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.profile.entity.Profile;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	List<Notice> findAllByWriter(final Profile profile);
}
