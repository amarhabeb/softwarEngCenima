package org.example.entities;

import javax.persistence.OneToOne;

public class Payment {
    private double amount;
    @OneToOne(targetEntity = Order.class)
    private Order order;
    @OneToOne(targetEntity = Customer.class)
    private Customer customer;

    public Payment(){}

    public Payment(double amount, Order order, Customer customer) {
        this.amount = amount;
        this.order = order;
        this.customer = customer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
