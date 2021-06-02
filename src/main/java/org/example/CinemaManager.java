package org.example;

public class CinemaManager extends Manager{
    int cinema;
    public CinemaManager(int ID, String name, String phoneNum, String email, String userName, String password, int cinema) {
        super(ID, name, phoneNum, email, userName, password);
        this.cinema = cinema;
    }

    public int getCinema(){
        return this.cinema;
    }
}
