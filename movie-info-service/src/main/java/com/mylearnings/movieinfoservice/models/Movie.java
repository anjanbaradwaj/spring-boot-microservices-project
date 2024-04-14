package com.mylearnings.movieinfoservice.models;

public class Movie {
    private String movieId;
    private String name;

    private String description;

    public Movie(String movieId, String name, String description) {
        this.movieId = movieId;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
