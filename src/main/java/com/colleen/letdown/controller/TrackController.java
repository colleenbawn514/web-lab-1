package com.colleen.letdown.controller;

import com.colleen.letdown.dao.PlaylistDAO;
import com.colleen.letdown.dao.TrackDAO;
import com.colleen.letdown.entities.Playlist;
import com.colleen.letdown.entities.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@Validated
@RequestMapping("/api/tracks")
public class TrackController {
    @Autowired
    private TrackDAO trackDAO;

    @Autowired
    private PlaylistDAO playlistDAO;

    @ResponseBody
    @GetMapping("/get")
    public Optional<Track> getTrack(@RequestParam long id){
        Optional<Track> track = trackDAO.findById(id);

        return track;
    }
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> createTrack(@Valid Track track, @RequestParam long playlistId, BindingResult bindingResult){
        Playlist playlist = playlistDAO.findById(playlistId).orElse(null);

        Track compareTrack= trackDAO.findByName(track.getName());
        if((compareTrack)!=null && compareTrack.getAuthor().equals(track.getAuthor())){
            return new ResponseEntity<>(compareTrack, HttpStatus.OK);
        }
        track.setPlaylist(playlist);
        trackDAO.save(track);

        return new ResponseEntity<>(track, HttpStatus.OK);
    }
}
