package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DemoWebfluxApplication {

	/**
	 * http://localhost:8080/auth/signup {"username": "sysout", "password":"admin123", "roles": [{"name": "ROLE_ADMIN"}]}
	 * http://localhost:8080/auth/signin {"username": "sysout", "password":"admin123"}
	 * */
	
	public static void main(String[] args) {
		SpringApplication.run(DemoWebfluxApplication.class, args);
	}

}
