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
		System.out.println("transaction start");
		final Profile writer = profileRepository.findById(profileId)
			.orElseThrow(NoSuchProfileException::new);

		System.out.println(writer.getNickName() + " " + writer.getEmail() + " " + writer.getAge()) ;
		notice.assignWriter(writer);

		return noticeRepository.save(notice);
	}

	@Override
	public List<Notice> getNoticeList() {
		return null;
	}

	@Override
	public Notice getNoticeById(final Long noticeId) {
		return noticeRepository.findById(noticeId)
			.orElseThrow(NoSuchNoticeException::new);
	}

	@Override
	public Notice updateNotice(final Notice notice) {
		return null;
	}

	@Override
	public void deleteNoticeById(final Long noticeId) {

	}

	@Override
	public List<Notice> getNoticeListByProfile(final Long profileId) {
		return null;
	}

	@Override
	public List<Notice> getLikedNoticeListByProfile(final Long profileId) {
		return null;
	}
}
