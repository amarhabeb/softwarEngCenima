package org.example;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;




public class ShowsHandler {

	// to load all shows from the database
	public static List<Show> loadShows(Session session) throws Exception{
		try {
            //Transaction transaction = session.beginTransaction();
            session.getTransaction().begin();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Show> query = builder.createQuery(Show.class);
			query.from(Show.class);
            //session.refresh(Show.class);
			List<Show> data = session.createQuery(query).getResultList();
			System.out.println(data.size());

			//transaction.commit();
            session.getTransaction().commit();
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
	
	// to add a show to the database
	public static boolean addShow(Session session, Show show) throws Exception{
		try {
			Transaction transaction = session.beginTransaction();
			session.save(show);
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
	
	// delete show from data base
	public static boolean deleteShow(Session session, int show_id){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Show> update_query = builder.createCriteriaUpdate(Show.class);
            Root<Show> root=update_query.from(Show.class);
            update_query.set("status", "NOT_AVAILABLE");
            update_query.where(builder.equal(root.get("ID"),show_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            transaction.commit();
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
	
	// update price of a show in the data base
	public static boolean updatePrice(Session session, int show_id, double newPrice){
		try {
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Show> update_query=builder.createCriteriaUpdate(Show.class);
            Root<Show> root=update_query.from(Show.class);
            update_query.set("price", newPrice);
            update_query.where(builder.equal(root.get("ID"),show_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            transaction.commit();
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
	
	// update time of a show in the data base
    public static boolean updateTime(Session session, int show_id, String newTime){
        try {
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Show> update_query=builder.createCriteriaUpdate(Show.class);
            Root<Show> root=update_query.from(Show.class);
            update_query.set("time", newTime);
            update_query.where(builder.equal(root.get("ID"),show_id));
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
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        } 
    }

}
