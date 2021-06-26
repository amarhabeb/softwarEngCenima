package org.example.entities;

import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

public class Package extends Order{
    @OneToMany
    private List<Ticket> tickets;

    public Package(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Package(LocalDate orderDate, boolean status, double price, int payment, int refund, boolean active, List<Ticket> tickets) {
        super(orderDate, status, price, payment, refund, active);
        this.tickets = tickets;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
