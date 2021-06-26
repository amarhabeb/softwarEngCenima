package org.example.entities;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

public class Ticket extends Order{
    @ManyToOne
    private Cinema cinema;
    private Seat seat;
    public Ticket(Cinema cinema, Seat seat, int ID, String orderDate, boolean status, double price, int payment, int refund, boolean active) {
        super(ID, orderDate, status, price, payment, refund, active);
        this.cinema=cinema;
        this.seat=seat;
    }

}
