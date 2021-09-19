package com.example.demo.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Playlist;
import com.example.demo.repository.PlaylistRespository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/playlist")
public class PlaylistController {

	@Autowired
	PlaylistRespository repository;

	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	@GetMapping
	public Flux<Playlist> getPlaylist() {
		return repository.findAll();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/{id}")
	public Mono<Playlist> getPlaylistId(@PathVariable String id) {
//		Mono<Playlist> e = repository.findById(id);
//		HttpStatus status = e != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return repository.findById(id);
	}

	@PostMapping
	public Mono<Playlist> insert(@RequestBody Playlist playlist) {
		return repository.save(playlist);
	}

	@PutMapping
	public Mono<Playlist> update(@RequestBody Playlist playlist) {
		return repository.save(playlist);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable String id) {
		repository.deleteById(id).subscribe();
	}

	@GetMapping(value = "/webflux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Tuple2<Long, Playlist>> getPlaylistByWebflux() {

		System.out.println("---Start get Playlists by WEBFLUX--- " + LocalDateTime.now());
		Flux<Long> interval = Flux.interval(Duration.ofSeconds(10));
		Flux<Playlist> playlistFlux = repository.findAll();

		return Flux.zip(interval, playlistFlux);

	}

	@GetMapping(value = "/mvc")
	public List<String> getPlaylistByMvc() throws InterruptedException {

		System.out.println("---Start get Playlists by MVC--- " + LocalDateTime.now());

		List<String> playlistList = new ArrayList<>();
		playlistList.add("Java 8");
		playlistList.add("Spring Security");
		playlistList.add("Github");
		playlistList.add("Deploy de uma aplicação java no IBM Cloud");
		playlistList.add("Bean no Spring Framework");
		TimeUnit.SECONDS.sleep(15);

		return playlistList;

	}

}
