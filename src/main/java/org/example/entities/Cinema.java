package org.example.entities;

import org.example.entities.Movie;
import org.example.entities.Show;

import javax.persistence.*;
import java.awt.*;
import java.util.List;
@Entity
@Table(name ="cinema")

public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    
    String branch_name;
    
    @OneToMany(targetEntity = Show.class)
    private List<Show> shows;
    @OneToMany(targetEntity = Movie.class)
    private List<Movie> movies;
    @OneToMany(targetEntity = Hall.class)
    private List<Hall> halls;

    public Cinema() {
    }

    public Cinema(List<Show> shows, List<Movie> movies, List<Hall> halls) {
        this.shows = shows;
        this.movies = movies;
        this.halls = halls;
    }

    public void addMovie(Movie Movie){
        movies.add(Movie);
    }

    public void addShow(Show sh){
        shows.add(sh);
    }

    public void deleteMovie(Movie M){
        movies.remove(M);
    }

    public void addHall(Hall h){
        halls.add(h);
    }

    public void deleteHall(Hall h){
        halls.remove(h);
    }

    public void deleteShow(Show sh){
        shows.remove(sh);
    }

    public List<Show> getShows() {
        return shows;
    }

    public List<Movie> getMovies() {
        return movies;
    }
    public List<Hall> getHalls(){
        return halls;
    }
    
    public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
}
