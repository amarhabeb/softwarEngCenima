package org.example.entities;

import java.time.LocalDate;
import java.util.Timer;

public class Link extends Order{
    private String link;
    private Timer timer;

    public Link(String link, Timer timer, LocalDate orderDate, boolean status, double price, Payment payment, int refund, boolean active) {
        super(orderDate, status, price, payment, refund, active);
        this.link = link;
        this.timer = timer;
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
