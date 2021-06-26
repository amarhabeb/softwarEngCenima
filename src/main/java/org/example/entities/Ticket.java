package org.example.entities;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

public class Ticket extends Order{
    @ManyToOne
    private Cinema cinema;
    private Seat seat;
    public Ticket(Cinema cinema, Seat seat, int ID, String orderDate, boolean status, double price, int payment, int refund, boolean active) {
        super( orderDate, status, price, payment, refund, active);
        this.cinema=cinema;
        this.seat=seat;
    }
    public Ticket(){
        super();
    }

    public Cinema getCinema() {
        return cinema;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
