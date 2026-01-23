package com.example.demo.security.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.security.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByTokenHash(String tokenHash);

	// Delete expired tokens
	long deleteByExpiresAtBefore(Instant now);

	// Optional: also delete revoked tokens (even if not expired)
	long deleteByRevokedTrue();

}
