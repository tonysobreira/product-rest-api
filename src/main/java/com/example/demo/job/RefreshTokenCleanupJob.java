package com.example.demo.job;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.security.repository.RefreshTokenRepository;

@Component
@Profile("prod")
public class RefreshTokenCleanupJob {

	private static final Logger log = LoggerFactory.getLogger(RefreshTokenCleanupJob.class);

	private final RefreshTokenRepository refreshTokenRepository;

	public RefreshTokenCleanupJob(RefreshTokenRepository refreshTokenRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
	}

	/**
	 * Runs every day at 03:30. Adjust the cron to your preference.
	 */
	@Scheduled(cron = "0 30 3 * * *")
	@Transactional
	public void purgeExpiredTokens() {
		Instant now = Instant.now();
		long deleted = refreshTokenRepository.deleteByExpiresAtBefore(now);

		if (deleted > 0) {
			log.info("RefreshToken cleanup: deleted {} expired tokens", deleted);
		} else {
			log.debug("RefreshToken cleanup: no expired tokens to delete");
		}
	}

	/**
	 * Optional: If you want to remove revoked tokens too (e.g., weekly).
	 */
	@Scheduled(cron = "0 0 4 * * MON")
	@Transactional
	public void purgeRevokedTokens() {
		long deleted = refreshTokenRepository.deleteByRevokedTrue();

		if (deleted > 0) {
			log.info("RefreshToken cleanup: deleted {} revoked tokens", deleted);
		} else {
			log.debug("RefreshToken cleanup: no revoked tokens to delete");
		}
	}

}
