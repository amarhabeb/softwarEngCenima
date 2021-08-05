package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "ordrs")
abstract public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int ID;
    protected LocalDateTime orderDate;
    protected boolean status; //true if available, false if canceled
    protected double price;
    @Column(length = 500)
    protected int customer_id;
    protected boolean active;   //true if active, false if "deleted" from database
    public static int orderId_counter=12;

    public Order( double price, int cusomer_id) {
        super();
        this.orderDate = LocalDateTime.now();
        this.status = true;
        this.price = price;
        this.customer_id=cusomer_id;
        this.active = true;
        this.orderId_counter++;
    }
    public Order() {
        super();
        this.status = true;
        this.active = true;
        this.orderId_counter++;

    }

    public static int getOrderId_counter() {
        return orderId_counter;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public int getId_counter(){
        return orderId_counter;
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

    public int getCusomer_id() {
        return customer_id;
    }

    public void setCusomer_id(int cusomer_id) {
        this.customer_id = cusomer_id;
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


