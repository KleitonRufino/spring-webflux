package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.LoginResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenProvider;

import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/signin")
	public Mono<ResponseEntity<LoginResponse>> signin(@RequestBody LoginRequest request) {
		return userRepository.findByUsername(request.getUsername())
				.filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
				.map(user -> ResponseEntity.ok(new LoginResponse(tokenProvider.generateToken(user))))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
	}

	@PostMapping("/signup")
	public Mono<ResponseEntity<User>> signUp(@RequestBody User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user).map(dbUser -> ResponseEntity.ok(dbUser))
				.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
	}

}