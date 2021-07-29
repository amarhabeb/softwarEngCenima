package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "orders")
abstract public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int ID;
    protected LocalDateTime orderDate;

    protected boolean status; //true if valid, false if canceled
    protected double price;
    @OneToOne(targetEntity = Payment.class, cascade = CascadeType.ALL)
    protected Payment payment;
    @OneToOne(targetEntity = Refund.class, cascade = CascadeType.ALL)
    protected Refund refund;
    protected boolean active;   //true if active, false if "deleted" from database

    public Order( LocalDateTime orderDate, boolean status, double price, Payment payment, Refund refund, boolean active) {
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
        this.status = true;
        this.active = true;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
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

    public  Payment getPayment() {
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
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
}


