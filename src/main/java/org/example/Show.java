package org.example;


import java.io.Serializable;

import javax.persistence.*;


@SuppressWarnings("serial")
@Entity
@Table(name = "shows")
public class Show implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private String date;
    private String time;
    private boolean isOnline;
    private String status;  //can be AVAILABLE / NOT_AVAILABLE
    private double price;
    //private Movie movie;
    private String movie_name;	// later will be a movie object instead
    private int hall_number;	// later will be a hall object instead
    private int cinema_number;	// later will be a cinema object instead

    public Show(String date, String time, boolean isOnline, String status, double price, String movie_name, int hall_number, int cinema_number) {
        super();
        this.date = date;
        this.time = time;
        this.isOnline = isOnline;
        this.status = status;
        this.price = price;
        this.movie_name = movie_name;
        this.hall_number = hall_number;
        this.cinema_number = cinema_number;
    }

    public Show() {
        super();
    }

    public int getID() {
        return ID;
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String isStatus() {
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

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public int getHall_number() {
        return hall_number;
    }

    public void setHall_number(int hall_number) {
        this.hall_number = hall_number;
    }

    public int getCinema_number() {
        return cinema_number;
    }

    public void setCinema_number(int cinema_number) {
        this.cinema_number = cinema_number;
    }

    //    public boolean soldOut(){
//
//    }

//    public boolean isSoon(){
//
//    }
}

