package org.example.entities;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.*;


@SuppressWarnings("serial")
@Entity
@Table(name = "shows")
public class Show implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private LocalDate date;
    private LocalTime time;
    private boolean isOnline;
    private String status;  //can be AVAILABLE / NOT_AVAILABLE
    private double price;
    private Movie movie;
    private Hall hall;
    private Cinema cinema;
    
    
    
    public Show(LocalDate date, LocalTime time, boolean isOnline, String status, double price, Movie movie, Hall hall, Cinema cinema) {
        super();
        this.date = date;
        this.time = time;
        this.isOnline = isOnline;
        this.status = status;
        this.price = price;
        this.movie = movie;
        this.hall = hall;
        this.cinema = cinema;
    }

    public int getID() {
        return ID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

    public boolean soldOut(){
        if(hall.isFull()){
            return true;
        }
        return false;
    }

}

