package org.example.entities;


import javax.persistence.*;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
//import javax.swing.*;
//import java.awt.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

@Entity
@Table(name ="movie")

public class Movie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private int ID;
    private String name_en;
    private String name_heb;
    private String director;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> cast;
    @Column(length = 5000)
    private String summary;
    private LocalDate launch_date;
    //private Boolean is_new;
    private int price;
    @Column(length = 5000)
    private String image;
    private String status; // can be AVAILABLE / NOT_AVAILABLE
    private boolean availableOnline;
    @OneToMany(targetEntity = Show.class, cascade = CascadeType.ALL)
    private List<Show> shows;
    @ManyToOne(targetEntity = Cinema.class)
    private Cinema cinema;

    public Movie() {    
    	this.status = "AVAILABLE";
    }


    public Movie(String name_en, String name_heb, String director, List<String> cast, String summary,
                 LocalDate lanuch_date,  String image, List<Show> shows, boolean availableOnline, Cinema cinema) {
        this.name_en = name_en;
        this.name_heb = name_heb;
        this.director = director;
        this.cast=cast;
        this.summary = summary;
        this.launch_date = lanuch_date;
        //this.is_new = is_new;
        this.image = image;
        this.shows = shows;
        this.availableOnline=true;
        this.status = "AVAILABLE";
        this.price= (int)Math.floor(Math.random()*(100-50+1)+50);
        this.cinema=cinema;
    }

    public LocalDate getLaunch_date() {
        return launch_date;
    }

    public void setLaunch_date(LocalDate launch_date) {
        this.launch_date = launch_date;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_heb() {
        return name_heb;
    }

    public void setName_heb(String name_heb) {
        this.name_heb = name_heb;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDate getLanuch_date() {
        return launch_date;
    }

    public void setLanuch_date(LocalDate launch_date) {
        this.launch_date = launch_date;
    }

    public boolean isAvailableOnline() {
        return availableOnline;
    }

    public void setAvailableOnline(boolean availableOnline) {
        this.availableOnline = availableOnline;
    }


    public   String getImage() {
        return image;
    }

    public void setImage( String image) {
        this.image = image;
    }

    public int getId() {
        return ID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }
    public void addActor(String actor){
        cast.add(actor);
    }
    public void deleteActor(String actor){
        cast.remove(actor);
    }
    public void addShow(Show sh){
        shows.add(sh);
    }
    public void deleteShow(Show sh){
        shows.remove(sh);
    }
    
    @Override
    public String toString() {
        return name_en;
    }


	public int getID() {
		return ID;
	}
	

}
