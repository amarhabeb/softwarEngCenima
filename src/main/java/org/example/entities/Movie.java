package org.example.entities;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
@Entity
@Table(name ="movie")

public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private String name_en;
    private String name_heb;
    private String director;
    private List<String> cast;
    private String summary;
    private LocalDate lanuch_date;
    private Boolean is_new;
    private String image;
    private String status; // can be AVAILABLE / NOT_AVAILABLE
    @OneToMany(targetEntity = Show.class)
    private List<Show> shows;

    public Movie() {    }


    public Movie(String name_en, String name_heb, String director, List<String> cast, String summary, LocalDate lanuch_date, Boolean is_new, String image, List<Show> shows) {
        this.name_en = name_en;
        this.name_heb = name_heb;
        this.director = director;
        this.cast=cast;
        this.summary = summary;
        this.lanuch_date = lanuch_date;
        this.is_new = is_new;
        this.image = image;
        this.shows = shows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIs_new() {
        return is_new;
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
        return lanuch_date;
    }

    public void setLanuch_date(LocalDate lanuch_date) {
        this.lanuch_date = lanuch_date;
    }


    public void setIs_new(Boolean is_new) {
        this.is_new = is_new;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return ID;
    }

    public void setImage(String image) {
        this.image = image;
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

    //calculate number of days between the movie's launch date and today, if less than 7, it's soon
    public boolean isSoon(){
        long diff= ChronoUnit.DAYS.between(LocalDate.now(), lanuch_date);
        if(diff<=7){
            return true;
        }
        return false;
    }
}
