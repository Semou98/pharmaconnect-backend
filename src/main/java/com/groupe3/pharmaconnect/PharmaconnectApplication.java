package com.groupe3.pharmaconnect;

import com.groupe3.pharmaconnect.entities.AppUser;
import com.groupe3.pharmaconnect.enums.AppUserRole;
import com.groupe3.pharmaconnect.repositories.AppUserRepository;
import com.groupe3.pharmaconnect.services.appUser.AppUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Set;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class PharmaconnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PharmaconnectApplication.class, args);
	}

	@Bean
	CommandLineRunner start(
			AppUserRepository appUserRepository,
			AppUserService appUserService,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
			// Création des utilisateurs initiaux avec différents rôles
			createInitialUsers(appUserRepository, appUserService, passwordEncoder);
		};
	}

	private void createInitialUsers(
			AppUserRepository appUserRepository,
			AppUserService appUserService,
			PasswordEncoder passwordEncoder
	) {
		// Structure des utilisateurs initiaux
		Map<String, InitialUser> initialUsers = Map.of(
				"admin@pharmaconnect.com", new InitialUser(
						"Admin User",
						"admin123",
						Set.of(AppUserRole.ADMIN)
				),
				"pharmacist@pharmaconnect.com", new InitialUser(
						"Test Pharmacist",
						"pharma123",
						Set.of(AppUserRole.PHARMACIST)
				),
				"client@pharmaconnect.com", new InitialUser(
						"Test Client",
						"client123",
						Set.of(AppUserRole.CLIENT)
				),
				"delegue@pharmaconnect.com", new InitialUser(
						"Test Délégué",
						"delegue123",
						Set.of(AppUserRole.DELEGUE_MEDICAL)
				),
				"superadmin@pharmaconnect.com", new InitialUser(
						"Super Admin",
						"super123",
						Set.of(AppUserRole.ADMIN, AppUserRole.PHARMACIST)
				)
		);

		// Création des utilisateurs
		initialUsers.forEach((email, user) -> {
			try {
				if (!appUserRepository.existsByEmail(email)) {
					AppUser newUser = new AppUser();
					newUser.setEmail(email);
					newUser.setName(user.name);
					newUser.setPassword(passwordEncoder.encode(user.password));
					newUser.setRoles(user.roles);
					newUser.setEnabled(true);

					appUserRepository.save(newUser);
					log.info("Created user: {} with roles: {}", email, user.roles);
				} else {
					log.info("User already exists: {}", email);
				}
			} catch (Exception e) {
				log.error("Error creating user {}: {}", email, e.getMessage());
			}
		});
	}

	// Classe utilitaire pour la structure des utilisateurs initiaux
	@AllArgsConstructor
	private static class InitialUser {
		String name;
		String password;
		Set<AppUserRole> roles;
	}
}
