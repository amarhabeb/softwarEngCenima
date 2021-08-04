package org.example.Controllers;

import org.example.entities.Show;
import org.example.entities.UpdatePriceRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

public class UpdatePriceRequestController {

    public static List<UpdatePriceRequest> loadRequests(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UpdatePriceRequest> query = builder.createQuery(UpdatePriceRequest.class);
            Root<UpdatePriceRequest> root=query.from(UpdatePriceRequest.class);
            query.where(builder.equal(root.get("active"),true));
            List<UpdatePriceRequest> data = session.createQuery(query).getResultList();
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
    public static boolean addRequest(Session session, UpdatePriceRequest upr) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(upr);
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
    public static boolean deleteRequest(Session session, int request_id){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<UpdatePriceRequest> update_query=builder.createCriteriaUpdate(UpdatePriceRequest.class);
            Root<UpdatePriceRequest> root=update_query.from(UpdatePriceRequest.class);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"),request_id));
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

    public static boolean approveRequest(Session session, UpdatePriceRequest upRequest){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<UpdatePriceRequest> update_query=builder.createCriteriaUpdate(UpdatePriceRequest.class);
            Root<UpdatePriceRequest> root=update_query.from(UpdatePriceRequest.class);

            update_query.set("approved", true);
            update_query.where(builder.equal(root.get("ID"),upRequest.getID()));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            session.clear();
            transaction.commit();

            CriteriaUpdate<Show> update_query2=builder.createCriteriaUpdate(Show.class);
            Root<Show> root2=update_query2.from(Show.class);
            update_query2.set("price",upRequest.getUpdatedPrice());
            update_query2.where(builder.equal(root2.get("ID"),upRequest.getShow_id()));
            Transaction transaction2 = session.beginTransaction();
            session.createQuery(update_query2).executeUpdate();
            session.clear();
            transaction2.commit();
            session.clear();
            
            deleteRequest(session, upRequest.getID());
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
    public static boolean declineRequest(Session session, int request_id){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<UpdatePriceRequest> update_query=builder.createCriteriaUpdate(UpdatePriceRequest.class);
            Root<UpdatePriceRequest> root=update_query.from(UpdatePriceRequest.class);
            update_query.set("approved", false);
            update_query.where(builder.equal(root.get("ID"),request_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            session.clear();
            transaction.commit();
            session.clear();
            
            deleteRequest(session, request_id);
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
