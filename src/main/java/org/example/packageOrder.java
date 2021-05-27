package org.example;

public class packageOrder extends order {
    private int amount;


    public packageOrder(int ID, String orderDate, int payment, double price){
        this.ID = ID;
        this.orderDAte = orderDate;
        this.payment = payment;
        this.price = price;
        this.refund = 0;
        this.status = true;
        this.amount = 20;
        this.active = true;
    }
}
