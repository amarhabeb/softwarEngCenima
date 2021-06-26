package org.example.entities;

import org.example.entities.Movie;
import org.example.entities.Show;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedList;

public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @OneToMany(targetEntity = Show.class)
    private LinkedList<Show> shows;
    @OneToMany(targetEntity = Movie.class)
    private LinkedList<Movie> movies;

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
