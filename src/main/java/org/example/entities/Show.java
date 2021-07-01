package org.example.entities;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.*;

import org.hibernate.cfg.Configuration;


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
    @ManyToOne(targetEntity = Movie.class)
    private Movie movie;
    @OneToOne(targetEntity = Hall.class)
    private Hall hall;
	private org.example.entities.Link link;
    private Cinema cinema1;
    
    public Show(LocalDate date, LocalTime time, boolean isOnline, String status, double price, Movie movie, Hall hall) {
        super();
        this.date = date;
        this.time = time;
        this.isOnline = isOnline;
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


    public boolean soldOut(){
        if(hall.isFull()){
            return true;
        }
        return false;
    }

	public Link getLinks() {
		// TODO Auto-generated method stub
		return link;
	}

	public void setCinema(Cinema cinema) {
this.cinema1=cinema;		
	}

}

