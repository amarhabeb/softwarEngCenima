package org.example.entities;

import org.example.entities.Manager;

import javax.persistence.OneToOne;

public class CinemaManager extends Manager {
    @OneToOne
    private Cinema cinema;
    public CinemaManager(int ID, String name, String phoneNum, String email, String userName, String password, Cinema cinema) {
        super(ID, name, phoneNum, email, userName, password);
        this.cinema = cinema;
    }

    public Cinema getCinema(){
        return this.cinema;
    }
}
