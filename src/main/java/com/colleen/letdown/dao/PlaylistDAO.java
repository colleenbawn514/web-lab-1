package com.colleen.letdown.dao;

import com.colleen.letdown.entities.Playlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistDAO extends CrudRepository<Playlist, Long> {
    Playlist findByName (String name);
}