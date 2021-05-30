package org.example;

import java.util.LinkedList;

public class cinema {
    int ID;
    private LinkedList<show> shows;
    private LinkedList<Movie> movies;

    public void addMovie(Movie Movie){
        movies.add(Movie);
    }

    public void addShow(show sh){
        shows.add(sh);
    }

    public void deleteMovie(Movie M){
        movies.remove(M);
    }

    public void deleteShow(show sh){
        shows.remove(sh);
    }

    public LinkedList<show> getShows() {
        return shows;
    }

    public LinkedList<Movie> getMovies() {
        return movies;
    }
}
