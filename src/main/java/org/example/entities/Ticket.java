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
    //@ManyToOne(targetEntity = Cinema.class)
    private int show_id;
    private int cinema_id;
    private int hall_id;
    private  int seat_id;
    private LocalDateTime show_time;
    public Ticket(int cinema_id, int hall_id,int seat_id, int show_id,LocalDateTime show_time,
                  double price, Payment payment) {
        super(price, payment);
        this.cinema_id=cinema_id;
        this.seat_id=seat_id;
        this.show_id=show_id;
        this.hall_id=hall_id;
        this.show_time=show_time;
    }

    public Ticket(){
        super();
    }

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

    public int getHall_id() {
        return hall_id;
    }

    public void setHall_id(int hall_id) {
        this.hall_id = hall_id;
    }

    public LocalDateTime getShow_time() {
        return show_time;
    }

    public void setShow_time(LocalDateTime show_time) {
        this.show_time = show_time;
    }
}
