package com.example.notice.controller;

import static com.example.notice.controller.dto.NoticeDto.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notice.domain.notice.entity.Notice;
import com.example.notice.domain.notice.entity.Slug;
import com.example.notice.domain.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;

	@PostMapping("/{profileId}")
	public ResponseEntity<NoticeResponse> createNotice(@RequestBody CreateNoticeRequest createRequest,
													   @PathVariable Long profileId){
		final Notice notice = noticeService.createNotice(createRequest.convert(), profileId);

		return new ResponseEntity<>(NoticeResponse.convert(notice), HttpStatus.CREATED);
	}

	@GetMapping("/{slug}")
	public ResponseEntity<NoticeResponse> getNotice(@PathVariable Slug slug){
		final Notice notice = noticeService.getNoticeBySlug(slug);

		return new ResponseEntity<>(NoticeResponse.convert(notice), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<NoticeResponses> getNotices(){
		final List<Notice> notices = noticeService.getNoticeList();

		return new ResponseEntity<>(NoticeResponses.convert(notices), HttpStatus.OK);
	}


}
