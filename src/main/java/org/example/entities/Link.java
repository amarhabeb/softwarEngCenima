package org.example.entities;

import java.time.LocalDate;
import java.util.Timer;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="link")

public class Link extends Order{
    private String link;
    private Timer timer;
    private int movie_id;  //This also gives us Shows times to display in the link message

    public Link(String link, Timer timer, int movie_id, LocalDate orderDate, boolean status, double price, Payment payment, Refund refund, boolean active) {
        super(orderDate, status, price, payment, refund, active);
        this.link = link;
        this.timer = timer;
        this.movie_id=movie_id;
    }

    public Link(){
        super();
    }
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
