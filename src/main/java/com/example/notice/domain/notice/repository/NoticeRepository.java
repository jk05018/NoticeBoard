package com.example.notice.domain.notice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Slug;
import com.example.notice.domain.user.entity.Profile;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

	List<Notice> findAllByWriter(final Profile writer);

	Optional<Notice> findBySlug(final Slug slug);

	void deleteBySlug(final Slug slug);
}
