package com.example.demo.security.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.InvalidRefreshTokenException;
import com.example.demo.security.dto.request.LoginRequest;
import com.example.demo.security.dto.request.RegisterRequest;
import com.example.demo.security.dto.response.TokenResponse;
import com.example.demo.security.model.AppUser;
import com.example.demo.security.model.RefreshToken;
import com.example.demo.security.model.Role;
import com.example.demo.security.repository.RefreshTokenRepository;
import com.example.demo.security.repository.UserRepository;
import com.example.demo.util.TokenHashing;

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
		log.info("Auth: login attempt username='{}'", req.username());

		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
		} catch (AuthenticationException ex) {
			log.warn("Auth: login failed username='{}'", req.username());
			throw ex;
		}

		AppUser user = userRepo.findByUsername(req.username()).orElseThrow();

		String access = jwtService.generateAccessToken(user.getUsername(),
				user.getRoles().stream().map(Enum::name).toList());
		String refresh = createRefreshToken(user);

		log.info("Auth: login success username='{}'", user.getUsername());
		return new TokenResponse(access, refresh);
	}

	@Transactional
	public TokenResponse refresh(String rawRefreshToken) {
		log.info("Auth: refresh attempt");

		String hash = TokenHashing.sha256Hex(rawRefreshToken);
		RefreshToken rt = refreshRepo.findByTokenHash(hash).orElseThrow(() -> {
			log.warn("Auth: refresh failed (invalid token)");
			return new InvalidRefreshTokenException("Invalid refresh token");
		});

		if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now())) {
			log.warn("Auth: refresh failed (revoked/expired) user='{}'", rt.getUser().getUsername());
			throw new InvalidRefreshTokenException("Refresh token expired or revoked");
		}

		rt.setRevoked(true);

		AppUser user = rt.getUser();
		String newAccess = jwtService.generateAccessToken(user.getUsername(),
				user.getRoles().stream().map(Enum::name).toList());
		String newRefresh = createRefreshToken(user);

		log.info("Auth: refresh success user='{}'", user.getUsername());
		return new TokenResponse(newAccess, newRefresh);
	}

	@Transactional
	public void logout(String rawRefreshToken) {
		String hash = TokenHashing.sha256Hex(rawRefreshToken);
		refreshRepo.findByTokenHash(hash).ifPresentOrElse(rt -> {
			rt.setRevoked(true);
			log.info("Auth: logout user='{}'", rt.getUser().getUsername());
		}, () -> log.warn("Auth: logout attempt with invalid token"));
	}

	private String createRefreshToken(AppUser user) {
		String raw = generateRefreshTokenValue();

		RefreshToken rt = new RefreshToken();
		rt.setUser(user);
		rt.setTokenHash(TokenHashing.sha256Hex(raw));
		rt.setExpiresAt(Instant.now().plusMillis(refreshExpMs));
		rt.setRevoked(false);

		refreshRepo.save(rt);
		return raw; // return ONLY to the client
	}

	private String generateRefreshTokenValue() {
		byte[] bytes = new byte[64];
		new SecureRandom().nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

}
