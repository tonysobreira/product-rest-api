package com.example.demo.security.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.example.demo.security.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final JwtProperties props;

	private final SecretKey key;

	public JwtService(JwtProperties props) {
		this.props = props;
		this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(String username, Collection<String> roles) {
		Instant now = Instant.now();
		return Jwts.builder().subject(username).claim("roles", roles).issuedAt(Date.from(now))
				.expiration(Date.from(now.plusMillis(props.getAccessExpirationMs()))).signWith(key).compact();
	}

	public Jws<Claims> parse(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
	}

}
