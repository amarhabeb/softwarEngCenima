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
    
	public Ticket getTicket() {
		return ticket;
	}
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
}
