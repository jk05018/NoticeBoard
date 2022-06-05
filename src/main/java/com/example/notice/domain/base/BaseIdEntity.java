package com.example.notice.domain.base;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@MappedSuperclass
@EqualsAndHashCode
public abstract class BaseIdEntity {

	/* 엔터티 식별 번호 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
