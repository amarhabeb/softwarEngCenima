package org.example.entities;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="ticket")

public class Ticket extends Order{
    @ManyToOne(targetEntity = Cinema.class)
    //private Cinema cinema;
    private int show_id;
    private int cinema_id;
    //private Seat seat;
    private  int seat_id;
    public Ticket(int cinema_id, int seat_id, int show_id, LocalDateTime orderDate, boolean status, double price, Payment payment, Refund refund, boolean active) {
        super( orderDate, status, price, payment, refund, active);
        this.cinema_id=cinema_id;
        this.seat_id=seat_id;
        this.show_id=show_id;
    }
   /* public Ticket(Cinema cinema, int seat_id, int show_id, int ID, LocalDate orderDate, boolean status, double price, Payment payment, Refund refund, boolean active) {
        super( orderDate, status, price, payment, refund, active);
        this.cinema=cinema;
        this.seat_id=seat_id;
        this.show_id=show_id;
    }*/
    public Ticket(){
        super();
    }
/*
    public Cinema getCinema() {
        return cinema;
    }
 public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
*/

    public int getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(int seat_id) {
        this.seat_id = seat_id;
    }

    public int getShow_id() {
        return show_id;
    }

    public void setShow_id(int show_id) {
        this.show_id = show_id;
    }

    public int getCinema_id() {
        return cinema_id;
    }

    public void setCinema_id(int cinema_id) {
        this.cinema_id = cinema_id;
    }
}
