package com.colleen.letdown.dao;

import com.colleen.letdown.entities.Track;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackDAO extends CrudRepository<Track, Long> {
    Track findByName (String name);
    Track findByAuthor (String author);
}
