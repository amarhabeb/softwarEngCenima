package org.example.entities;

abstract class Order {
    protected int ID;
    protected String orderDate;
    protected boolean status;
    protected double price;
    protected int payment;
    protected int refund;
    protected boolean active;

    public Order() {
    }

    public Order(int ID, String orderDate, boolean status, double price, int payment, int refund, boolean active) {
        this.ID = ID;
        this.orderDate = orderDate;
        this.status = status;
        this.price = price;
        this.payment = payment;
        this.refund = refund;
        this.active = active;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}


