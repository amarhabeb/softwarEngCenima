package org.example;

import java.util.LinkedList;

public class cinema {
    int ID;
    private LinkedList<show> shows;
    private LinkedList<movie> movies;

    public void addMovie(movie Movie){
        movies.add(Movie);
    }

    public void addShow(show sh){
        shows.add(sh);
    }

    public void deleteMovie(movie M){
        movies.remove(M);
    }

    public void deleteShow(show sh){
        shows.remove(sh);
    }

    public LinkedList<show> getShows() {
        return shows;
    }

    public LinkedList<movie> getMovies() {
        return movies;
    }
}
