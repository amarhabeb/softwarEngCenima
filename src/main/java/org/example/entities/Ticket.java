package org.example.entities;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="ticket")

public class Ticket extends Order{
    @ManyToOne(targetEntity = Cinema.class)
    private Cinema cinema;
    private int show_id;
    //private Seat seat;
    private  int seat_id;
	private Show show;
    public Ticket(Cinema cinema, int seat_id, int show_id, int ID, LocalDate orderDate, boolean status, double price, Payment payment, Refund refund, boolean active) {
        super( orderDate, status, price, payment, refund, active);
        this.cinema=cinema;
        this.seat_id=seat_id;
        this.show_id=show_id;
    }
    public Ticket(){
        super();
    }

    public Cinema getCinema() {
        return cinema;
    }


    public int getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(int seat_id) {
        this.seat_id = seat_id;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
	public void setShow(Show show) {
this.show=show;		
	}


}
