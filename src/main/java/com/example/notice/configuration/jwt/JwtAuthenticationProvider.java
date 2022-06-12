package com.example.notice.configuration.jwt;

import static org.hibernate.validator.internal.util.TypeHelper.*;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.notice.domain.user.entity.Password;
import com.example.notice.domain.user.entity.User;
import com.example.notice.domain.user.entity.Username;
import com.example.notice.domain.user.service.UserService;
import com.example.notice.domain.user.service.UserServiceImpl;

public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final Jwt jwt;
	private final UserService userService;

	public JwtAuthenticationProvider(Jwt jwt, UserService userService) {
		this.jwt = jwt;
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken)authentication;
		return processUserAuthentication(
			String.valueOf(jwtAuthentication.getPrincipal()),
			jwtAuthentication.getCredentials()
		);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return isAssignable(JwtAuthenticationToken.class, authentication);
	}

	private Authentication processUserAuthentication(String principal, String credentials) {
		try {
			User user = userService.login(new Username(principal), new Password(credentials));
			List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
			String token = getToken(Username.toString(user.getUsername()), authorities);
			JwtAuthenticationToken authenticated =
				new JwtAuthenticationToken(new JwtAuthentication(token, Username.toString(user.getUsername())), null,
					authorities);
			authenticated.setDetails(user);
			return authenticated;
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException(e.getMessage());
		} catch (DataAccessException e) {
			throw new AuthenticationServiceException(e.getMessage(), e);
		}
	}

	private String getToken(final String username, final List<GrantedAuthority> authorities) {
		String[] roles = authorities.stream()
			.map(GrantedAuthority::getAuthority)
			.toArray(String[]::new);
		return jwt.sign(Jwt.Claims.from(username, roles));
	}

}
