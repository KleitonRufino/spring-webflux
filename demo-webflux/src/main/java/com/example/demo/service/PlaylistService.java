package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Playlist;
import com.example.demo.repository.PlaylistRespository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaylistService {

	@Autowired
	PlaylistRespository pr;

	public Flux<Playlist> findAll() {
		return pr.findAll();
	}

	public Mono<Playlist> findById(String id) {
		return pr.findById(id);
	}

	public Mono<Playlist> save(Playlist playlist) {
		return pr.save(playlist);
	}

	public Mono<Void> delete(String id) {
		return pr.deleteById(id);
	}
}
