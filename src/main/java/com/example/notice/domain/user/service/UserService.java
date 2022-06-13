package com.example.notice.domain.user.service;

import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.Profile;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;

public interface UserService {

	User registerUser(final User user);

	User login(final Username principal, final Password credentials);

	User makeProfile(final Username username, final Profile profile);

	User findByUsername(final Username username);
}
