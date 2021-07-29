 package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name ="Refund")
public class Refund implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private double amount;
    private int order_id;  //by this we can get customer_id too
    private int complaint_id;  //is -1 if the reason of the refund is not a complaint (cancelling a show)
    private LocalDateTime date;

    public Refund(double amount, int order_id, int complaint_id, LocalDateTime date) {
        this.amount = amount;
        this.order_id = order_id;
        this.complaint_id=complaint_id;
        this.date = date;
    }
    public Refund(){

    }

    public int getID() {
        return ID;
    }

    public double getAmount() {
        return amount;
    }

    public int getOrder_id() {
        return order_id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(int complaint_id) {
        this.complaint_id = complaint_id;
    }
	public void setID(int iD) {
		ID = iD;
	}
}
