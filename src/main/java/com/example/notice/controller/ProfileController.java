package com.example.notice.controller;

import static com.example.notice.controller.dto.ProfileDto.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notice.controller.dto.ProfileDto;
import com.example.notice.domain.profile.entity.Profile;
import com.example.notice.domain.profile.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileController {

	private final ProfileService profileService;

	@PostMapping
	public ResponseEntity<ProfileResponse> createProfile(@RequestBody ProfileCreateRequest profileCreateRequest){
		final Profile createdProfile = profileService.createProfile(profileCreateRequest.convert());
		return new ResponseEntity<>(ProfileResponse.convert(createdProfile), HttpStatus.CREATED);
	}

	@GetMapping("/hello")
	public String hello(){
		return "hello";
	}

}
