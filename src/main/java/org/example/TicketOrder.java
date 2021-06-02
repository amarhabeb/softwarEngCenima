package org.example;

public class TicketOrder extends Order {


    public TicketOrder(int ID, String orderDate, int payment, double price){
        this.ID = ID;
        this.orderDAte = orderDate;
        this.payment = payment;
        this.price = price;
        this.refund = 0;
        this.status = true;
        this.active = true;
    }


}
