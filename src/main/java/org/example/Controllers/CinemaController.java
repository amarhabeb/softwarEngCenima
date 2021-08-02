package org.example.Controllers;

import org.example.entities.Cinema;
import org.example.entities.Hall;
import org.example.entities.Regulations;
import org.example.entities.Show;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class CinemaController {
    public static List<Cinema> loadCinemas(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Cinema> query = builder.createQuery(Cinema.class);
            Root<Cinema>root=query.from(Cinema.class);
            query.where(builder.equal(root.get("active"),true));
            List<Cinema> data = session.createQuery(query).getResultList();
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

    public static boolean addCinema(Session session, Cinema cinema) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(cinema);
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

    public static boolean deleteCinema(Session session, int cinema_id){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Cinema> update_query = builder.createCriteriaUpdate(Cinema.class);
            Root<Cinema> root=update_query.from(Cinema.class);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"),cinema_id));
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
    public static List<Show> loadCinemaShows(Session session, int cinema_id){
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Cinema> query = builder.createQuery(Cinema.class);
            Root<Cinema>root=query.from(Cinema.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("ID"),cinema_id);
            predicates[1]=builder.equal(root.get("active"),true);
            query.where(predicates);
            List<Show> data = session.createQuery(query).getResultList().get(0).getShows();
            for(Show sh: data){
                if(sh.getStatus()=="NOT_AVAILABLE")
                    data.remove(sh);
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
}
