package com.colleen.letdown.controller;

import com.colleen.letdown.dao.PlaylistDAO;
import com.colleen.letdown.dao.TrackDAO;
import com.colleen.letdown.dao.UserDAO;
import com.colleen.letdown.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Validated
@RequestMapping("/api/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistDAO playlistDAO;

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TrackDAO trackDAO;

    @ResponseBody
    @GetMapping("/get")
    public ResponseEntity<?> getPlaylist(@RequestParam long id){
        Playlist playlist = playlistDAO.findById(id).orElse(null);
        if(playlist==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/get_by_user_id")
    public List<Playlist> getByUserId(@RequestParam long userId){
       /* List<PlaylistByUser> playlistByUsers = playlistByUserDAO.findByUserId(userId);
        List<Playlist> playlists = Collections.emptyList();
        for(PlaylistByUser playlistByUser:playlistByUsers){
            playlists.add(playlistByUser.getPlaylist());
        }
        return playlists;*/
        return Collections.EMPTY_LIST;
    }

    /*@ResponseBody
    @GetMapping("/get_tracks")
    public List<TrackByPlaylist> getTracksByPlaylistId(@RequestParam long id){
        List<TrackByPlaylist> trackByPlaylists = trackByPlaylistDAO.findByPlaylistId(id);
        ArrayList<Track> tracks = new ArrayList<>();
        for(TrackByPlaylist trackByPlaylist:trackByPlaylists){
            tracks.add(trackByPlaylist.getTrack());
            System.out.println(trackByPlaylist.getTrack());
            System.out.println(trackByPlaylist.getTrackId());
        }
        return trackByPlaylists;
    }*/
    /*@ResponseBody
    @PostMapping("/add_track")
    public ResponseEntity<?> addTrack(@RequestParam long id,@RequestParam long trackId){
        Track track = trackDAO.findById(trackId).orElse(null);
        Playlist playlist = playlistDAO.findById(id).orElse(null);
        if(track==null || playlist==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TrackByPlaylist trackByPlaylist = new TrackByPlaylist();
        trackByPlaylist.setTrack(track);
        trackByPlaylist.setPlaylist(playlist);
        trackByPlaylistDAO.save(trackByPlaylist);

        return new ResponseEntity<>(HttpStatus.OK);
    }*/
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> createPlaylist(@Valid Playlist playlist,@RequestParam long userId){
        User user = userDAO.findById(userId).orElse(null);
        playlist.setUser(user);
        playlistDAO.save(playlist);

        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }
}
