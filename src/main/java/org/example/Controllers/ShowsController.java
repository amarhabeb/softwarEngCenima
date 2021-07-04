package org.example.Controllers;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.example.entities.Show;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;




public class ShowsController {
		
	// to load all shows from the database

	public static List<Show> loadShows(Session session) throws Exception{
		try {
            Transaction transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Show> query = builder.createQuery(Show.class);
			query.from(Show.class);
			List<Show> data = session.createQuery(query).getResultList();
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
	
	// to add a show to the database
	@SuppressWarnings("exports")
	public static boolean addShow(Session session, Show show) throws Exception{
		try {
			Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Show> query = builder.createQuery(Show.class);
            query.from(Show.class);
            List<Show> data = session.createQuery(query).getResultList();
            for(int i=0;i< data.size();i++){
                if(data.get(i).getHall()== show.getHall() && data.get(i).getDate() ==show.getDate()){
                    return  false;
                }
            }
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
	@SuppressWarnings("exports")
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

    public static List<Show> loadMovieShow(Session session, int movie_id){
        try {

            List<Show> shows = null;
            return shows;

        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Show> shows = null;
        return shows;
    }
	
	// update price of a show in the data base
	@SuppressWarnings("exports")
	public static boolean updatePrice(Session session, int show_id, double newPrice){
		try {
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Show> update_query=builder.createCriteriaUpdate(Show.class);
            Root<Show> root=update_query.from(Show.class);
            update_query.set("price", newPrice);
            update_query.where(builder.equal(root.get("ID"),show_id));
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
	
	// update time of a show in the data base
    @SuppressWarnings("exports")
	public static boolean updateTime(Session session, int show_id, LocalTime newTime){
        try {
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Show> update_query=builder.createCriteriaUpdate(Show.class);
            Root<Show> root=update_query.from(Show.class);
            update_query.set("time", newTime);
            update_query.where(builder.equal(root.get("ID"),show_id));
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
