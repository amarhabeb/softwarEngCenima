package org.example.entities;

import javax.persistence.ManyToOne;
import java.time.LocalDate;

public class TicketReport extends Report {
    @ManyToOne
    private Cinema cinema;

    public TicketReport(LocalDate date, Cinema cinema){
        this.ID = ID;
        this.date = date;
        this.cinema = cinema;
    }
    public TicketReport(){super();}

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
}
