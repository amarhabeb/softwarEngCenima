package org.example.Controllers;

import org.example.entities.Hall;
import org.example.entities.Regulations;
import org.example.entities.Seat;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.List;

public class HallController {
    public static List<Hall> loadHalls(Session session) throws Exception {
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Hall> query = builder.createQuery(Hall.class);
            Root<Hall> root = query.from(Hall.class);
            query.where(builder.equal(root.get("active"), true));
            List<Hall> data = session.createQuery(query).getResultList();
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

    public static boolean addHall(Session session, Hall hall) throws Exception {
        try {
            if(!session.getTransaction().isActive()) {
                Transaction transaction = session.beginTransaction();
                session.save(hall);
                session.flush();
                transaction.commit();

                return true;
            }
            return false;
        } catch (Exception exception) {
            if (session != null || session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean deleteHall(Session session, int hall_id) {
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Hall> update_query = builder.createCriteriaUpdate(Hall.class);
            Root<Hall> root = update_query.from(Hall.class);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"), hall_id));
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
            System.err.println("An errxor occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }
    public static List<Seat> loadSeats(Session session, int hall_id) throws Exception {
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Hall> query = builder.createQuery(Hall.class);
            Root<Hall> root = query.from(Hall.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("active"), true);
            predicates[1]=builder.equal(root.get("ID"), hall_id);
            query.where(predicates);
            List<Seat> data = session.createQuery(query).getResultList().get(0).getSeats();
            for(Seat s:data){
                if(!s.isActive())
                    data.remove(s);
            }
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

    public static List<Seat> loadLineSeats(Session session, int hall_id, int line) throws Exception {
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Hall> query = builder.createQuery(Hall.class);
            Root<Hall> root = query.from(Hall.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("active"), true);
            predicates[1]=builder.equal(root.get("ID"), hall_id);
            query.where(predicates);
            List<Seat> data = session.createQuery(query).getResultList().get(0).getSeats();
            for(Seat s:data){
                if(!s.isActive() || s.getLine()!=line)
                    data.remove(s);
            }
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

    public static boolean limitMaxSeats(Session session, int hall_id, int maxSeats) {
        try {
            session.clear();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Hall> update_query = builder.createCriteriaUpdate(Hall.class);
            Root<Hall> root = update_query.from(Hall.class);
            update_query.set("maxSeats", maxSeats);
            update_query.where(builder.equal(root.get("ID"), hall_id));
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

    public static boolean resetMaxSeats(Session session, int hall_id, int capacity) {
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Hall> update_query = builder.createCriteriaUpdate(Hall.class);
            Root<Hall> root = update_query.from(Hall.class);
            update_query.set("maxSeats", capacity);
            update_query.where(builder.equal(root.get("ID"), hall_id));
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

