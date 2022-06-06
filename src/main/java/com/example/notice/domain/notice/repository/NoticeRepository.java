package com.example.notice.domain.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notice.domain.notice.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
