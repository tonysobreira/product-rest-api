package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.security.model.AppUser;
import com.example.demo.security.model.Role;
import com.example.demo.security.repository.UserRepository;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) {
		createAdmin();
		createUser("user1");
		createUser("user2");
		createUser("user3");
	}

	private void createAdmin() {
		if (userRepository.existsByUsername("admin")) {
			return;
		}

		AppUser admin = new AppUser();
		admin.setUsername("admin");
		admin.setPasswordHash(passwordEncoder.encode("admin123"));

		admin.getRoles().add(Role.ROLE_ADMIN);
		admin.getRoles().add(Role.ROLE_USER);

		userRepository.save(admin);

		System.out.println("✔ Admin user created");
	}

	private void createUser(String username) {
		if (userRepository.existsByUsername(username)) {
			return;
		}

		AppUser user = new AppUser();
		user.setUsername(username);
		user.setPasswordHash(passwordEncoder.encode("password"));

		user.getRoles().add(Role.ROLE_USER);

		userRepository.save(user);

		System.out.println("✔ User created: " + username);
	}
}
