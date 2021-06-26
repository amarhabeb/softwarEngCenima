package org.example.entities;

public class TicketReport extends Report {
    private Cinema cinema;

    public TicketReport(int ID, String date, Cinema cinema){
        this.ID = ID;
        this.date = date;
        this.cinema = cinema;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
}
