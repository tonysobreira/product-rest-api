package com.example.demo.security.service;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.security.dto.LoginRequest;
import com.example.demo.security.dto.RegisterRequest;
import com.example.demo.security.dto.TokenResponse;
import com.example.demo.security.model.AppUser;
import com.example.demo.security.model.RefreshToken;
import com.example.demo.security.model.Role;
import com.example.demo.security.repository.RefreshTokenRepository;
import com.example.demo.security.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepo;

	private final RefreshTokenRepository refreshRepo;

	private final PasswordEncoder encoder;

	private final AuthenticationManager authManager;

	private final JwtService jwtService;

	private final long refreshExpMs;

	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	public AuthService(UserRepository userRepo, RefreshTokenRepository refreshRepo, PasswordEncoder encoder,
			AuthenticationManager authManager, JwtService jwtService,
			@Value("${jwt.refresh-expiration-ms}") long refreshExpMs) {
		this.userRepo = userRepo;
		this.refreshRepo = refreshRepo;
		this.encoder = encoder;
		this.authManager = authManager;
		this.jwtService = jwtService;
		this.refreshExpMs = refreshExpMs;
	}

	@Transactional
	public void register(RegisterRequest req) {
		log.info("Register attempt for username='{}'", req.username());

		if (userRepo.existsByUsername(req.username())) {
			log.warn("Registration failed: username already exists '{}'", req.username());
			throw new IllegalArgumentException("Username already exists");
		}

		AppUser user = new AppUser();
		user.setUsername(req.username());
		user.setPasswordHash(encoder.encode(req.password()));
		user.getRoles().add(Role.ROLE_USER);

		userRepo.save(user);

		log.info("User registered successfully: '{}'", req.username());
	}

	@Transactional
	public TokenResponse login(LoginRequest req) {
		log.info("Login attempt for username='{}'", req.username());

		try {
			Authentication auth = authManager
					.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));

			UserDetails principal = (UserDetails) auth.getPrincipal();
			log.info("Username='{}'", principal.getUsername());

			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
			log.info("Authorities='{}'", authorities);
		} catch (AuthenticationException ex) {
			log.warn("Login failed for username='{}'", req.username());
			throw ex;
		}

		AppUser user = userRepo.findByUsername(req.username()).orElseThrow(() -> {
			log.error("Authenticated user not found in database: {}", req.username());
			return new IllegalStateException("User not found after authentication");
		});

		String access = jwtService.generateAccessToken(user.getUsername(),
				user.getRoles().stream().map(Enum::name).toList());

		String refresh = createRefreshToken(user);

		log.info("Login successful for username='{}'", user.getUsername());

		return new TokenResponse(access, refresh);
	}

	@Transactional
	public TokenResponse refresh(String refreshToken) {
		log.info("Refresh token request");

		RefreshToken rt = refreshRepo.findByToken(refreshToken).orElseThrow(() -> {
			log.warn("Invalid refresh token used");
			return new IllegalArgumentException("Invalid refresh token");
		});

		if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now())) {
			log.warn("Expired or revoked refresh token for user '{}'", rt.getUser().getUsername());
			throw new IllegalArgumentException("Refresh token expired or revoked");
		}

		// rotation
		rt.setRevoked(true);

		AppUser user = rt.getUser();

		String newAccess = jwtService.generateAccessToken(user.getUsername(),
				user.getRoles().stream().map(Enum::name).toList());

		String newRefresh = createRefreshToken(user);

		log.info("Refresh token rotated for user '{}'", user.getUsername());

		return new TokenResponse(newAccess, newRefresh);
	}

	@Transactional
	public void logout(String refreshToken) {
		refreshRepo.findByToken(refreshToken).ifPresentOrElse(rt -> {
			rt.setRevoked(true);
			log.info("User '{}' logged out", rt.getUser().getUsername());
		}, () -> log.warn("Logout attempt with invalid refresh token"));
	}

	private String createRefreshToken(AppUser user) {
		RefreshToken rt = new RefreshToken();
		rt.setUser(user);
		rt.setToken(UUID.randomUUID().toString() + "." + UUID.randomUUID()); // simple opaque token
		rt.setExpiresAt(Instant.now().plusMillis(refreshExpMs));
		rt.setRevoked(false);
		refreshRepo.save(rt);
		return rt.getToken();
	}

}
