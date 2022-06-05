package com.example.notice.domain.notice.entity;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.notice.domain.base.BaseIdEntity;
import com.example.notice.domain.profile.entity.Profile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseIdEntity {

	@Embedded
	private Body body;

	@ManyToOne(fetch = FetchType.LAZY)
	private Notice notice;

	@OneToOne(fetch = FetchType.LAZY)
	private Profile writer;

	@CreatedDate
	private LocalDateTime createdDate;

	@LastModifiedDate
	private LocalDateTime updatedDate;

	private Comment(final Body body, final Notice notice, final Profile writer) {
		Optional.ofNullable(body).ifPresent(safeBody -> this.body = safeBody);
		Optional.ofNullable(notice).ifPresent(safeNotice -> this.notice = safeNotice);
		Optional.ofNullable(writer).ifPresent(safeWriter -> this.writer = safeWriter);
	}

	public static Comment of(final Body body, final Notice notice, final Profile writer) {
		return new Comment(body, notice, writer);
	}

	public void updateBody(final Body body) {
		Optional.ofNullable(body).ifPresent(safeBody -> this.body = safeBody);
	}

}
