package org.example.entities;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.*;


@SuppressWarnings("serial")
@Entity
@Table(name = "shows")
public class Show implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shows_id")
    private int ID;
    private LocalDateTime dateTime;
    //private LocalTime time;
    //private boolean isOnline;
    private String status;  //can be AVAILABLE / NOT_AVAILABLE
    private double price;
    @ManyToOne(targetEntity = Movie.class, cascade = CascadeType.ALL)
    private Movie movie;
    @ManyToOne(targetEntity = Hall.class, cascade = CascadeType.ALL)
    private Hall hall;

    
    
    public Show(LocalDateTime date, String status, double price, Movie movie, Hall hall) {
        super();
        this.dateTime = date;
        //this.isOnline = isOnline;
        this.status = status;
        this.price = price;
        this.movie = movie;
        this.hall = hall;

    }

    public Show(){}

    public int getID() {
        return ID;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public void setDate(LocalDate date) {
    	this.setDateTime(LocalDateTime.of(date, this.getTime()));
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setTime(LocalTime time) {
    	this.setDateTime(LocalDateTime.of(this.getDate(), time));
    }

    /*public boolean getIsOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }*/

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


    public boolean soldOut(){
        if(hall.isFull()){
            return true;
        }
        return false;
    }

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

}

