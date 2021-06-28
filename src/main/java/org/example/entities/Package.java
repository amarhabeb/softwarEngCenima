package org.example.entities;

import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="package")

public class Package extends Order{
    @OneToMany
    private List<Ticket> tickets;

    public Package(LocalDate orderDate, boolean status, double price, Payment payment, Refund refund, boolean active, List<Ticket> tickets) {
        super(orderDate, status, price, payment, refund, active);
        this.tickets = tickets;
    }

    public Package(){super();}

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    /*public void addTicket(Ticket t){
        if(tickets.size()!=10) {
            tickets.add(t);
        }

    }
    public void deleteTicket(Ticket t){
        tickets.remove(t);
    }*/
}
