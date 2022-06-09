package com.example.notice.domain.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.repository.NoticeRepository;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.repository.ProfileRepository;
import com.example.notice.exception.NoSuchNoticeException;
import com.example.notice.exception.NoSuchProfileException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

	private final NoticeRepository noticeRepository;
	private final ProfileRepository profileRepository;

	@Override
	public Notice createNotice(final Notice notice, final Long profileId) {
		final Profile writer = profileRepository.findById(profileId)
			.orElseThrow(NoSuchProfileException::new);

		notice.assignWriter(writer);

		return noticeRepository.save(notice);
	}

	@Override
	public List<Notice> getNoticeList() {
		return noticeRepository.findAll();
	}

	@Override
	public Notice getNoticeById(final Long noticeId) {
		return noticeRepository.findById(noticeId)
			.orElseThrow(NoSuchNoticeException::new);
	}

	@Override
	public Notice updateNotice(final Notice updateNotice, final Long noticeId) {
		Notice notice = noticeRepository.findById(noticeId)
			.orElseThrow(NoSuchNoticeException::new);

		notice.updateNotice(updateNotice);

		return notice;
	}

	@Override
	public void deleteNoticeById(final Long noticeId) {
		noticeRepository.deleteById(noticeId);
	}

	@Override
	public List<Notice> getNoticeListByProfile(final Long profileId) {
		final Profile writer = profileRepository.findById(profileId)
			.orElseThrow(NoSuchProfileException::new);

		return noticeRepository.findAllByWriter(writer);
	}

	@Override
	public List<Notice> getLikedNoticeListByProfile(final Long profileId) {
		return null;
	}
}
