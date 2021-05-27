package org.example;

import java.util.Timer;

public class linkOrder extends order{
    String link;
    Timer timer;

    public linkOrder(int ID, String orderDate, int payment, double price, String link, Timer timer){
        this.ID = ID;
        this.orderDAte = orderDate;
        this.payment = payment;
        this.price = price;
        this.refund = 0;
        this.status = true;
        this.active = false;
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
