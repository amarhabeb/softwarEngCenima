package org.example;

public class TicketReport extends Report {
    private int cinema;

    public TicketReport(int ID, String date, int cinema){
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
