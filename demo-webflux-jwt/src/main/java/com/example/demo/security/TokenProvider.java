package com.example.demo.security;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider implements Serializable {

	private static final long serialVersionUID = 5240125635450006537L;

	@Value("${security.jwt.token.secret-key:secret}")
	private String secretKey = "secret";

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(User user) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		final List<String> authorities = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("roles", authorities);

		return Jwts.builder().setSubject(user.getUsername()).setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, secretKey).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(validity).compact();
	}

	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
}
