package org.example.entities;

import javax.persistence.ManyToOne;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="ticketreport")

public class TicketReport extends Report {
    private int ticket_id;

    public TicketReport(LocalDate date, int ticket_id){
        super(date);
        this.ticket_id=ticket_id;

    }
    public TicketReport(){super();}

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }
}
