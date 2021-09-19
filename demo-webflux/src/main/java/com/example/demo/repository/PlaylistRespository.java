package com.example.demo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.demo.entity.Playlist;

public interface PlaylistRespository extends ReactiveMongoRepository<Playlist, String>{

}
