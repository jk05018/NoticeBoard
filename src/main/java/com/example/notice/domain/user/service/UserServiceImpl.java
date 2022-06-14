package com.example.notice.domain.user.service;

import static com.google.common.base.Preconditions.*;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.Profile;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.example.notice.domain.user.repository.UserRepository;
import com.example.notice.exception.NoSuchUserException;
import com.example.notice.exception.NoticeProjectException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public User login(final Username principal, final Password credentials) {
		checkArgument(Objects.nonNull(principal), "principal must be provided.");
		checkArgument(Objects.nonNull(credentials), "credentials must be provided.");

		final User user = findByUsername(principal);
		user.checkPassword(passwordEncoder, Password.toString(credentials));
		return user;
	}

	@Override
	public User makeProfile(final Username username, final Profile profile) {
		if(profile == null){
			throw new IllegalStateException();
		}
		final User user = userRepository.findByUsername(username)
			.orElseThrow(NoSuchUserException::new);

		user.assignProfile(profile);

		return user;
	}

	@Override
	public User registerUser(final User user) {
		user.getPassword().encodePassword(passwordEncoder);

		return userRepository.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByUsername(final Username username) {
		checkArgument(Objects.nonNull(username), "loginId must be provided.");

		return userRepository.findByUsername(username)
			.orElseThrow(() -> new NoSuchUserException("Could not found user for " + username));
	}
}
