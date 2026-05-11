package com.usuarios.usuarios_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.usuarios.usuarios_service.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> {})
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth

						// Permite el login con google
						.requestMatchers(
								"/login/**",
								"/oauth2/**")
						.permitAll()

						.requestMatchers(
							"/api/v1/usuarios/oauth-success",
							"/api/v1/usuarios/me",
							"/api/v1/usuarios/completar-perfil"
						).authenticated()

						.requestMatchers(
							"/api/v1/usuarios",
							"/api/v1/usuarios/**"
						).hasRole("ADMIN")

						// Esta protege todos los demas endpoints
						.anyRequest().authenticated())
				// Se habilita el OAuth2 Login y despues redirecciona
				.oauth2Login(oauth2 -> oauth2
						.defaultSuccessUrl(
								"https://ss-frontend-theta.vercel.app",
								true
						)
				)
				.addFilterBefore(
					jwtAuthFilter,
					UsernamePasswordAuthenticationFilter.class
				);

		return http.build();
	}

}
