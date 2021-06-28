package org.example.Controllers;

import org.example.entities.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

public class TicketsController {
    public static List<Ticket> loadTickets(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            query.from(Ticket.class);
            List<Ticket> data = session.createQuery(query).getResultList();
            transaction.commit();
            return data;
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }
    }
    public static List<Ticket> loadCustomersTickets(Session session,int cust_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> TicketRoot = query.from(Ticket.class);
            Join<Ticket, Payment> payment = TicketRoot.join("payment");
            Join<Payment,Customer> customerid = TicketRoot.join("id");
            query.from(Ticket.class);
            query.select(TicketRoot).where(builder.equal(customerid.get("id"),cust_id));
            List<Ticket> data = session.createQuery(query).getResultList();
            transaction.commit();
            return data;
        } catch (Exception exception) {
            if (session != null) {  
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }
    }
    public static Boolean addTicket(Ticket newticket,Session session) throws Exception {
        try {

            Transaction transaction = session.beginTransaction();
            session.save(newticket);
            session.flush();
            transaction.commit();
            return true;
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }

//    public static  List<Seat> loadSeats(int seat_id, Session session) throws Exception {
//        try {
//            Transaction transaction = session.beginTransaction();
//            CriteriaBuilder builder = session.getCriteriaBuilder();
//            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
//            query.from(Ticket.class);
//            List<Ticket> data = session.createQuery(query).getResultList();
//            transaction.commit();
//            return data;
//        } catch (Exception exception) {
//            if (session != null) {
//                session.getTransaction().rollback();
//            }
//            System.err.println("An error occured, changes have been rolled back.");
//            exception.printStackTrace();
//            return null;
//        }
//
//    }
    public static TicketMessage sendTicketAsMessage(TicketMessage mesage) throws Exception {
        return mesage;
        // we have to change it
    }

}
