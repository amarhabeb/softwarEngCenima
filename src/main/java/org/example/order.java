package org.example;

abstract class order {
    protected int ID;
    protected String orderDAte;
    protected boolean status;
    protected double price;
    protected int payment;
    protected int refund;
    protected boolean active;

    public order() {
    }

    public order(int ID, String orderDAte, double price, int payment) {
        this.ID = ID;
        this.orderDAte = orderDAte;
        this.status = true;
        this.price = price;
        this.payment = payment;
        this.refund = 0;
        this.active = true;
    }
}


