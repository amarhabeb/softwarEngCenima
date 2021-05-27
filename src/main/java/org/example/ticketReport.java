package org.example;

public class ticketReport extends report {
    private int cinema;

    public ticketReport(int ID, String date, int cinema){
        this.ID = ID;
        this.date = date;
        this.cinema = cinema;
    }

    public int getCinema() {
        return cinema;
    }

    public void setCinema(int cinema) {
        this.cinema = cinema;
    }
}
