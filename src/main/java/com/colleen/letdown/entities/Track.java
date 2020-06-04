package com.colleen.letdown.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tracks")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @NotNull(message = "name can't be null")
    @NotEmpty(message = "name can't be empty")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull(message = "size can't be null")
    @DecimalMin(value = "0.01", message = "Min size must be 0.01Mb" )
    @DecimalMax(value = "100", message = "Max size must be 100Mb" )
    @Column(name = "size", nullable = false)
    private double size;

    @NotNull(message = "duration can't be null")
    @Min(value = 1, message = "Min duration must be 1sec" )
    @Max(value = 3600, message = "Max duration must be 3600sec" )
    @Column(name = "duration", nullable = false)
    private int duration;

    @NotNull(message = "name author can't be null")
    @NotEmpty(message = "name author can't be empty")
    @Size(min = 1, max = 255, message = "Name author must be between 1 and 255 characters")
    @Column(name = "author", length = 255, nullable = false)
    private String author;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString(){
        return String.format("Track[id=%s, name=%s, author=%s, size=%d, duration=%s]", id, name, author, size, duration);
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
