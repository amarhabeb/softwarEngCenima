package org.example.Controllers;

import org.example.entities.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
            System.err.println("An error occurred, changes have been rolled back.");
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
            System.err.println("An error occurred, changes have been rolled back.");
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
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }

    //cancel ticket in data base, calculate refund, add it to data base, then return it
    public static Refund cancelTicket(Session session, int ticket_id){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Ticket> update_query=builder.createCriteriaUpdate(Ticket.class);
            Root<Ticket> root=update_query.from(Ticket.class);
            update_query.set("status", false);
            update_query.where(builder.equal(root.get("ID"),ticket_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            session.clear();
            LocalDateTime dt=loadTicketShowTime(session, ticket_id);
            double price=loadTicketPrice(session, ticket_id);
            double r=calcRefund(dt);
            //if refund is 50%
            if (r==0.5){
                price*=0.5;
            }
            //if refund is 0, do nothing and return
            else if(r==0){
                transaction.commit();
                session.clear();
                return null;
            }
            //if refund is 100%
            Refund refund=new Refund(price, ticket_id, LocalDateTime.now());
            boolean answer= RefundController.addRefund(session,refund);

            transaction.commit();
            session.clear();
            if(answer){
                return refund;
            }
            else{
                return null;
            }
            // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }
    }
    public static LocalDateTime loadTicketShowTime(Session session, int ticket_id){
        try{
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Show> query = builder.createQuery(Show.class);
            Root<Ticket> TicketRoot=query.from(Ticket.class);
            Join<Ticket, Show> ticketShow = TicketRoot.join("show_id");
            //query.from(Show.class);
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
    public static double loadTicketPrice(Session session, int ticket_id){
        try{
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Show> query = builder.createQuery(Show.class);
            Root<Ticket> TicketRoot=query.from(Ticket.class);
            Join<Ticket, Show> ticketShow = TicketRoot.join("show_id");
            //query.from(Show.class);
            query.select(ticketShow).where(builder.equal(ticketShow.get("id"),ticket_id));
            List<Show> data = session.createQuery(query).getResultList();
            //Show data = session.createQuery(query).uniqueResult();
            double res=data.get(0).getPrice();
            transaction.commit();
            return res;
        }
        catch (Exception exception){
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return -1;
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
}
