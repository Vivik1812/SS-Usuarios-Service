package com.usuarios.usuarios_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import com.usuarios.usuarios_service.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Value("${app.gw_url}")
	private String GW_URL;
	private final JwtAuthenticationFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> {
				})
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						// Permite el login con google
						.requestMatchers(
								"/login/**",
								"/oauth2/**",
								"/actuator/**",
								"/api/v1/usuarios/oauth-success")
						.permitAll()

						.requestMatchers(
								"/api/v1/usuarios/me",
								"/api/v1/usuarios/completar/perfil")
						.authenticated()

						.requestMatchers(
								"/api/v1/usuarios",
								"/api/v1/usuarios/**")
						.hasRole("ADMIN")

						// Esta protege todos los demas endpoints
						.anyRequest().authenticated())
						.exceptionHandling(ex -> ex
							.authenticationEntryPoint((request, response, authException) ->{
								response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
							})
						)
				// Se habilita el OAuth2 Login y despues redirecciona
				.oauth2Login(oauth2 -> oauth2
						.defaultSuccessUrl(
								GW_URL,
								true))
				.addFilterBefore(
						jwtAuthFilter,
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
