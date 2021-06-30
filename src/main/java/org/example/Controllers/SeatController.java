package org.example.Controllers;


import org.example.entities.Seat;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

public class SeatController {
    public static List<Seat> loadSeats(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Seat> query = builder.createQuery(Seat.class);
            query.from(Seat.class);
            List<Seat> data = session.createQuery(query).getResultList();
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

    public static boolean addSeat(Session session, Seat seat) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(seat);
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

    public static boolean deleteSeat(Session session, int hall_id){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Seat> update_query = builder.createCriteriaUpdate(Seat.class);
            Root<Seat> root=update_query.from(Seat.class);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"),hall_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            transaction.commit();
            //session.clear();
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
}
