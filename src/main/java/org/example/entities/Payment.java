package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity

@Table(name ="payment")

public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int ID;
    private double amount;
    //@OneToOne(targetEntity = Order.class, cascade = CascadeType.ALL)
    //private Order order;
   // @OneToOne(targetEntity = Customer.class, cascade = CascadeType.ALL)
    //private Customer customer;
    private int customer_id;

    public Payment(){}

    public Payment(double amount, Order order, int customer_id) {
        this.amount = amount;
       // this.order = order;
        this.customer_id = customer_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

	public int getID() {
		return ID;
	}

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }
}
