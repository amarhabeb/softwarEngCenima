package org.example;
import javax.persistence.*;
import java.util.*;
@Entity
@Table(name ="showw")

public class show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String date;
    private String time;
    private Boolean online;
    private  Boolean status;
    private double price;
    @ManyToOne(targetEntity = movie.class)
    private movie movie;

    public show() {
        super();
    }

    public show(String date, String time, Boolean online, Boolean status, double price,movie movie) {
        super();
        this.date = date;
        this.time = time;
        this.online = online;
        this.status = status;
        this.price = price;
        this.movie=movie;

    }

    public movie getMovie() {
        return movie;
    }

    public void setMovie(movie movie) {
        this.movie = movie;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

