package org.example.entities;

import org.example.entities.Movie;
import org.example.entities.Show;

import java.util.LinkedList;

public class Cinema {
    int ID;
    private LinkedList<Show> shows;
    private LinkedList<Movie> movies;
    ///change

    public void addMovie(Movie Movie){
        movies.add(Movie);
    }

    public void addShow(Show sh){
        shows.add(sh);
    }

    public void deleteMovie(Movie M){
        movies.remove(M);
    }

    public void deleteShow(Show sh){
        shows.remove(sh);
    }

    public LinkedList<Show> getShows() {
        return shows;
    }

    public LinkedList<Movie> getMovies() {
        return movies;
    }
}
