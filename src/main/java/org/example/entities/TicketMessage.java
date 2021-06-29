package org.example.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="ticketmessage")

public class TicketMessage extends Message {
    private Ticket ticket;
    public TicketMessage(Ticket ticket){
        super("Details of your Ticket:");
        this.ticket=ticket;
    }
    public TicketMessage(){super();}
}
