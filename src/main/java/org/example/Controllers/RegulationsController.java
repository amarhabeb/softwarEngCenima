package org.example.Controllers;

import org.example.entities.Regulations;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

public class RegulationsController {

    public static List<Regulations> loadReagulations(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Regulations> query = builder.createQuery(Regulations.class);
            query.from(Regulations.class);
            List<Regulations> data = session.createQuery(query).getResultList();
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
    //used ONLY once for intiallization.. ONLY ONLY ONLY
    public static boolean addRegulations(Session session, Regulations reg) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(reg);
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
    public static boolean activateRegulations(Session session, int Y){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Regulations> update_query=builder.createCriteriaUpdate(Regulations.class);
            Root<Regulations> root=update_query.from(Regulations.class);
            update_query.set("status", true);
            update_query.where(builder.equal(root.get("Y"),Y));
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
    public static boolean deactivateRegulations(Session session){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Regulations> update_query=builder.createCriteriaUpdate(Regulations.class);
            Root<Regulations> root=update_query.from(Regulations.class);
            update_query.set("status", false);
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


}
