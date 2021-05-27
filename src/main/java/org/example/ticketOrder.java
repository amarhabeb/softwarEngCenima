package org.example;

public class ticketOrder extends order {


    public ticketOrder(int ID, String orderDate, int payment, double price){
        this.ID = ID;
        this.orderDAte = orderDate;
        this.payment = payment;
        this.price = price;
        this.refund = 0;
        this.status = true;
        this.active = true;
    }


}
