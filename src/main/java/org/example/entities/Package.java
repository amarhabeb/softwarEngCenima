package org.example.entities;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="package")

public class Package extends Order{
    @OneToMany(targetEntity = Ticket.class, cascade = CascadeType.ALL)
    private boolean[] tickets; //false if ticket is used
    private int counter;

    public Package(LocalDateTime orderDate, boolean status, double price, Payment payment, Refund refund, boolean active) {
        super(orderDate, status, price, payment, refund, active);
        counter=20;
        this.tickets = new boolean[20];
        for(int i=0; i<20;i++)
            tickets[i]=true;
    }

    public Package(){super();}

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
    public void useTicket(){
        if(!isEmpty()){
            for(int i=0;i<20;i++){
                if(tickets[i]) {
                    tickets[i] = false;
                    counter--;
                }
            }
        }

    }
    public boolean isEmpty(){
        if(counter==0)
            return true;
        else
            return false;
    }
}
