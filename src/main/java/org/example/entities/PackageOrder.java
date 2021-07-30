package org.example.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name ="package")

public class PackageOrder extends Order{
    //@OneToMany(cascade = CascadeType.ALL)
    @OrderColumn(name = "tickets")
    private boolean[] tickets; //false if ticket is used
    private int counter;

    public PackageOrder(double price, int customer_id) {
        super(price, customer_id);
        counter=20;
        this.tickets = new boolean[20];
        for(int i=0; i<20;i++)
            tickets[i]=true;
    }

    public PackageOrder(){super();}

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

	public boolean[] getTickets() {
		return tickets;
	}

	public void setTickets(boolean[] tickets) {
		this.tickets = tickets;
	}
}
