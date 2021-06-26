package org.example.entities;

import java.util.Timer;

public class Link extends Order{
    private String link;
    private Timer timer;

    public Link(String link, Timer timer, int ID, String orderDate, boolean status, double price, int payment, int refund, boolean active) {
        super(ID, orderDate, status, price, payment, refund, active);
        this.link = link;
        this.timer = timer;
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
