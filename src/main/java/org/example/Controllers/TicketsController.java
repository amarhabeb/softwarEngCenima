package org.example.Controllers;

import org.example.entities.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TicketsController {
    public static List<Ticket> loadTickets(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root=query.from(Ticket.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("status"),true);
            predicates[1]=builder.equal(root.get("active"),true);
            query.where(predicates);
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
    public static List<Ticket> loadTicketsByShowID(Session session,int show_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root=query.from(Ticket.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.equal(root.get("status"),true);
            predicates[1]=builder.equal(root.get("active"),true);
            predicates[2]=builder.equal(root.get("show_id"),show_id);
            query.where(predicates);
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
            Root<Ticket> root=query.from(Ticket.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.equal(root.get("customer_id"),cust_id);
            predicates[1]=builder.equal(root.get("status"),true);
            predicates[2]=builder.equal(root.get("active"),true);
            query.where(predicates);
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
    public static Boolean addTicket(Session session, Ticket newticket) throws Exception {
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
    public static boolean cancelTicket(Session session, int ticket_id){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Ticket> update_query=builder.createCriteriaUpdate(Ticket.class);
            Root<Ticket> root=update_query.from(Ticket.class);
            update_query.set("status", false);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"),ticket_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            transaction.commit();
            session.clear();
            return true;
            // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }
    public static boolean cancelTicketByShowID(Session session, int show_id){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Ticket> update_query=builder.createCriteriaUpdate(Ticket.class);
            Root<Ticket> root=update_query.from(Ticket.class);
            update_query.set("status", false);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("show_id"),show_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            transaction.commit();
            session.clear();
            return true;
            // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }

//    private static Refund calcRefund(Session session, int ticket_id) throws Exception {
//        Refund refund = new Refund(0,ticket_id,0,LocalDateTime.now());
//        if(loadTicketShowTime(session,ticket_id).isAfter(LocalDateTime.now().plusHours(3))){
//            refund.setAmount(loadTicketPrice(session,ticket_id));
//            RefundController.addRefund(session,refund);
//        }
//        else if(loadTicketShowTime(session,ticket_id).isAfter(LocalDateTime.now().plusHours(1))){
//            refund.setAmount(loadTicketPrice(session,ticket_id)/2);
//            RefundController.addRefund(session,refund);
//        }
//        return refund;
//    }

    public static LocalDateTime loadTicketShowTime(Session session, int ticket_id){
        try{
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root=query.from(Ticket.class);
            query.where(builder.equal(root.get("ID"),ticket_id));
            List<Ticket> data = session.createQuery(query).getResultList();
            LocalDateTime res=data.get(0).getShow_time();
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
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root=query.from(Ticket.class);
            query.where(builder.equal(root.get("ID"),ticket_id));
            List<Ticket> data = session.createQuery(query).getResultList();
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
    public static int loadTicketCustomerID(Session session, int ticket_id){
        try{
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root=query.from(Ticket.class);
            query.where(builder.equal(root.get("ID"),ticket_id));
            List<Ticket> data = session.createQuery(query).getResultList();
            int res=data.get(0).getCusomer_id();
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

    public static List<Ticket> makeTicketsReportByMonth(Session session, int cinema_id, Integer month, Integer year) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root=query.from(Ticket.class);
            Predicate[] predicates=new Predicate[4];
            predicates[0]=builder.equal(root.get("cinema_id"),cinema_id);
            predicates[1]=builder.equal(root.get("active"),true);
            predicates[2]=builder.equal(builder.function("MONTH", Integer.class, root.get("orderDate")),month);
            predicates[3]=builder.equal(builder.function("YEAR", Integer.class, root.get("orderDate")),year);
            query.where(predicates);
            query.orderBy(builder.asc(root.get("orderDate")));
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
}
