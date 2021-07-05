package org.example.entities;

import org.example.entities.Manager;

import javax.persistence.OneToOne;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="cinemamanger")
public class CinemaManager extends Manager {
    private int cinema_id;
    public CinemaManager(String name, String phoneNum, String email, String userName, String password, int cinema_id) {
        super(name, phoneNum, email, userName, password, 2);
        this.cinema_id = cinema_id;
    }
    public CinemaManager(){
        super();
    }

    public int getCinema(){
        return this.cinema_id;
    }

    public void setCinema_id(int cinema_id) {
        this.cinema_id = cinema_id;
    }
}
