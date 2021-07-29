package org.example.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="ticketmessage")

public class TicketMessage extends Message {
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    private Ticket ticket;
    private String mail;
    public TicketMessage(Ticket ticket,String mail){
        super("Details of your Ticket:");
        this.ticket=ticket;
        this.mail=mail;
    }
    public TicketMessage(){super();}
}

