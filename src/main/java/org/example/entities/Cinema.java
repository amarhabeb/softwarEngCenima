package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name ="cinema")

public class Cinema implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    String branch_name;
    
    @OneToMany(targetEntity = Show.class, cascade = CascadeType.ALL)
    private List<Show> shows;
    @OneToMany(targetEntity = Movie.class, cascade = CascadeType.ALL)
    private List<Movie> movies;
    @OneToMany(targetEntity = Hall.class, cascade = CascadeType.ALL)
    private List<Hall> halls;

    private boolean active=true;

    public Cinema() {
        this.shows=new LinkedList<Show>();
        this.movies=new LinkedList<Movie>();
        this.halls=new LinkedList<Hall>();
        this.active=true;
    }
    
    public Cinema(String branch_name) {
        this.shows=new LinkedList<Show>();
        this.movies=new LinkedList<Movie>();
        this.halls=new LinkedList<Hall>();
        this.branch_name = branch_name;
        this.active=true;
    }

    public Cinema(List<Show> shows, List<Movie> movies, List<Hall> halls) {
        this.shows = shows;
        this.movies = movies;
        this.halls = halls;
        this.active=true;
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

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void setHalls(List<Hall> halls) {
        this.halls = halls;
    }

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isActive() {
		return active;
	}
	
	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
    public String toString() {
        return branch_name;
    }
}
