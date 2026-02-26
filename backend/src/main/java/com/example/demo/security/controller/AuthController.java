package com.example.demo.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.dto.request.LoginRequest;
import com.example.demo.security.dto.request.RefreshRequest;
import com.example.demo.security.dto.request.RegisterRequest;
import com.example.demo.security.dto.response.TokenResponse;
import com.example.demo.security.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req) {
		authService.register(req);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody LoginRequest req) {
		return authService.login(req);
	}

	@PostMapping("/refresh")
	public TokenResponse refresh(@Valid @RequestBody RefreshRequest req) {
		return authService.refresh(req.refreshToken());
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest req) {
		authService.logout(req.refreshToken());
		return ResponseEntity.noContent().build();
	}

}
