package com.colleen.letdown.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @NotNull(message = "login can't be null")
    @NotEmpty(message = "login can't be empty")
    @Size(min = 2, max = 64, message = "Login must be between 2 and 64 characters")
    @Column(name = "login", length = 64, nullable = false, unique = true)
    private String login;

    @JsonIgnore
    @NotNull(message = "password can't be null")
    @NotEmpty(message = "password can't be empty")
    @Size(min = 5, max = 128, message = "Password must be between 5 and 128 characters")
    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @NotNull(message = "name can't be null")
    @NotEmpty(message = "name can't be empty")
    @Size(min = 2, max = 128, message = "Name must be between 2 and 128 characters")
    @Column(name = "name", length = 128, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Playlist> playlists = new HashSet<>();
    @PrePersist
    public void prePersist() {
        if (playlists == null) {
            playlists = Collections.emptySet();
        }
    }
    @JsonGetter("playlists")
    public ArrayList<Long> getPlaylistIds() {
        ArrayList<Long> idPlaylist = new ArrayList<Long>();
        for(Playlist playlist:playlists){
            idPlaylist.add(playlist.getId());
        }
        return idPlaylist;
    }
    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return String.format("User[id=%s, login=%s, password=%s]", id, login, password);
    }

    public Set<Playlist> getPlaylists() {
        System.out.println("hjbjhbhbj");
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }
}
