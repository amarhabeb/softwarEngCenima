package org.example;

public class PackageOrder extends Order {
    private int amount;


    public PackageOrder(int ID, String orderDate, int payment, double price){
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
