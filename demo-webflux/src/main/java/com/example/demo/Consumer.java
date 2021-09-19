package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.entity.Playlist;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Consumer implements CommandLineRunner {

	WebClient client = WebClient.create("http://localhost:8080");

	@Override
	public void run(String... args) throws Exception {

		System.out.println("CONSUMER");
		
		Mono<Playlist> playlist = client.get().uri("/playlist/588fb87b-3f24-40c7-8c95-ac2dba57024b").retrieve()
				.bodyToMono(Playlist.class);
		playlist.subscribe(System.out::println);
		
		Flux<Playlist> playlists = client.get().uri("/playlist").retrieve().bodyToFlux(Playlist.class);
		playlists.subscribe(System.out::println);
		
	}

}
