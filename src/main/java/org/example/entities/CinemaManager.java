package org.example.entities;

import org.example.entities.Manager;

import javax.persistence.OneToOne;

public class CinemaManager extends Manager {
    @OneToOne
    private Cinema cinema;
    public CinemaManager(String name, String phoneNum, String email, String userName, String password, Cinema cinema) {
        super(name, phoneNum, email, userName, password);
        this.cinema = cinema;
    }
    public CinemaManager(){
        super();
    }

    public Cinema getCinema(){
        return this.cinema;
    }
}
