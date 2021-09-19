package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextRepository securityContextRepository;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
		String[] patterns = new String[] { "/auth/**" };
		return http.cors().disable().exceptionHandling().authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
			swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		})).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
			swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		})).and().csrf().disable().authenticationManager(authenticationManager)
				.securityContextRepository(securityContextRepository).authorizeExchange().pathMatchers(patterns)
				.permitAll().pathMatchers(HttpMethod.OPTIONS).permitAll().anyExchange().authenticated().and().build();

	}

}
