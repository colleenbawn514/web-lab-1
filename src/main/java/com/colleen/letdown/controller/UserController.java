package com.colleen.letdown.controller;

import com.colleen.letdown.dao.PlaylistDAO;
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
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@Validated
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PlaylistDAO playlistDAO;


    @GetMapping("/principal")
    public Principal getPrincipal(Principal principal){
        return principal;
    }

    @ResponseBody
    @GetMapping("/")
    public List<User> index(){
        List<User> users = StreamSupport
                .stream(userDAO.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return users;
    }
    @ResponseBody
    @GetMapping("/get")
    public Optional<User> getUser(@RequestParam long id){
        Optional<User> user = userDAO.findById(id);

        return user;
    }
    @ResponseBody
    @PostMapping("/add_playlist")
    public ResponseEntity<?> addTrack(@RequestParam long id,@RequestParam long playlistId){
        User user = userDAO.findById(id).orElse(null);
        Playlist playlist = playlistDAO.findById(playlistId).orElse(null);
        if(user==null || playlist==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playlist.setUser(user);
        playlistDAO.save(playlist);
        /*PlaylistByUser playlistByUser = new PlaylistByUser();
        playlistByUser.setPlaylist(playlist);
        playlistByUser.setUser(user);
        playlistByUserDAO.save(playlistByUser);
*/
        return new ResponseEntity<>(HttpStatus.OK);
    }
   /* @ResponseBody
    @GetMapping("/get_playlists")
    public List<PlaylistByUser> getTracksByPlaylistId(@RequestParam long id){
        List<PlaylistByUser> playlistByUsers = playlistByUserDAO.findByUserId(id);
        ArrayList<Playlist> playlists = new ArrayList<>();
        for(PlaylistByUser playlistByUser:playlistByUsers){
            playlists.add(playlistByUser.getPlaylist());
        }
        return playlistByUsers;
    }*/
    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            System.out.println("Create Failed");
        }
        if(userDAO.findByLogin(user.getLogin())!=null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userDAO.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
