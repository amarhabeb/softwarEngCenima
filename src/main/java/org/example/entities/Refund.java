package org.example.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name ="Refund")
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private double amount;
    private int order_id;  //by this we can get customer_id too
    private LocalDateTime date;

    public Refund(double amount, int order_id, LocalDateTime date) {
        this.amount = amount;
        this.order_id = order_id;
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
}
