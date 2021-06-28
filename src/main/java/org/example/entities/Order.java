package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "orders")
abstract public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int ID;
    protected LocalDate orderDate;
    protected boolean status;
    protected double price;
    @OneToOne(targetEntity = Payment.class)
    protected Payment payment;
    @OneToOne(targetEntity = Refund.class)
    protected Refund refund;
    protected boolean active;

    public Order( LocalDate orderDate, boolean status, double price, Payment payment, Refund refund, boolean active) {
        super();
        this.orderDate = orderDate;
        this.status = status;
        this.price = price;
        this.payment = payment;
        this.refund = refund;
        this.active = active;
    }
    public Order() {
        super();
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Refund getRefund() {
        return refund;
    }

    public void setRefund(Refund refund) {
        this.refund = refund;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}


