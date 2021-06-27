package org.example;

import java.util.LinkedList;

public class Cinema {
    int ID;
    String branch_name;
    private LinkedList<Show> shows;
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

	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	public void setShows(LinkedList<Show> shows) {
		this.shows = shows;
	}

	public void setMovies(LinkedList<Movie> movies) {
		this.movies = movies;
	}
}
