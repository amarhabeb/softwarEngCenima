package org.example.Controllers;

import org.example.entities.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
            Join<Payment, Customer> customerid = TicketRoot.join("id");
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
    public static TicketMessage sendTicketAsMessage(TicketMessage mesage) throws Exception {
        return mesage;
        // we have to change it
    }
    //cancel ticket function, MUST call refund function after calling this one
    public static boolean cancelTicket(Session session, int ticket_id){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Ticket> update_query=builder.createCriteriaUpdate(Ticket.class);
            Root<Ticket> root=update_query.from(Ticket.class);
            update_query.set("status", false);
            //Predicate[] predicates = new Predicate[2];
            //predicates[0]=builder.equal(root.get("ID"),ticket_id);
            //predicates[1]=builder.equal(root.get("ID"),ticket_id);
            update_query.where(builder.equal(root.get("ID"),ticket_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            session.clear();
            transaction.commit();
            session.clear();
            return true;
            // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }
    public static LocalDateTime loadTicketShowTime(Session session, int ticket_id){
        try{
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Show> query = builder.createQuery(Show.class);
            Root<Ticket> TicketRoot=query.from(Ticket.class);
            Join<Ticket, Show> ticketShow = TicketRoot.join("show_id");
            query.from(Show.class);
            query.select(ticketShow).where(builder.equal(ticketShow.get("id"),ticket_id));
            List<Show> data = session.createQuery(query).getResultList();
            //Show data = session.createQuery(query).uniqueResult();
            LocalDateTime res=data.get(0).getDate();
            transaction.commit();
            return res;
        }
        catch (Exception exception){
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }

    }
    public static double calcRefund(LocalDateTime DT){
        double refund=0;
        long days= ChronoUnit.DAYS.between(LocalDateTime.now(), DT);
        long hours=ChronoUnit.HOURS.between(LocalDateTime.now(), DT);
        long minutes=ChronoUnit.MINUTES.between(LocalDateTime.now(),DT);
        long seconds=ChronoUnit.SECONDS.between(LocalDateTime.now(),DT);
        if(days>=0){
            if(hours>=3){
                refund=1;
            }
            else if(hours>1 && hours<3){
                refund=0.5;
            }
        }
        return refund;
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

}
