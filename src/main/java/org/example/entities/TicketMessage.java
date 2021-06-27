package org.example.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="ticketmessage")

public class TicketMessage extends Message {

    public TicketMessage(int ID, String text) {
        super(text);
    }
    public TicketMessage(){super();}
}
