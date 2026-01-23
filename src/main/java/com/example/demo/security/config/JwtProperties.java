package com.example.demo.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "jwt")
@Configuration
public class JwtProperties {

	@NotBlank
	private String secret;

	@Min(60000)
	private long accessExpirationMs;

	@Min(60000)
	private long refreshExpirationMs;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getAccessExpirationMs() {
		return accessExpirationMs;
	}

	public void setAccessExpirationMs(long accessExpirationMs) {
		this.accessExpirationMs = accessExpirationMs;
	}

	public long getRefreshExpirationMs() {
		return refreshExpirationMs;
	}

	public void setRefreshExpirationMs(long refreshExpirationMs) {
		this.refreshExpirationMs = refreshExpirationMs;
	}

}
