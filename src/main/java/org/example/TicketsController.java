package org.example;

import org.example.entities.Order;
import org.example.entities.Ticket;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class TicketsController extends OrderController{



    public static List<Ticket> loadTickets(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            query.from(Ticket.class);
            List<Ticket> data = session.createQuery(query).getResultList();
            transaction.commit();
            return data;


        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }

    }

    public static List<Ticket> loadCutomersTicket(int cost_id,Session session) throws  Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();


            CriteriaQuery<Ticket> query = builder.createQuery(Ticket.class);
            Root<Ticket> root=query.from(Ticket.class);
            query.from(Ticket.class);
            query.where(builder.equal(root.get("price"),cost_id));
            List<Ticket> data = session.createQuery(query).getResultList();
            transaction.commit();
            return data;

        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }

    }







}
