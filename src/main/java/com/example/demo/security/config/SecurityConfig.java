package com.example.demo.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.filter.JwtAuthFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**", "/h2-console/**").permitAll()

						// If you want products GET open to all:
						.requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()

						// Only admins can write:
						.requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PATCH, "/api/v1/products/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("ADMIN")

						.anyRequest().authenticated())
				.headers(h -> h.frameOptions(frame -> frame.disable())) // H2 console
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		http.exceptionHandling(eh -> eh.authenticationEntryPoint(authenticationEntryPoint()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json");
			response.getWriter().write("{\"error\":\"UNAUTHORIZED\",\"message\":\"Authentication required\"}");
		};
	}

}
