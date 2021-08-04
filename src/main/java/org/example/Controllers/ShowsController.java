package org.example.Controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.criteria.*;

import org.example.OCSF.CinemaClient;
import org.example.entities.Cinema;
import org.example.entities.Hall;
import org.example.entities.Show;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class ShowsController {
		
	// to load all shows from the database

	public static List<Show> loadShows(Session session) throws Exception{
		try {
            Transaction transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Show> query = builder.createQuery(Show.class);
			Root<Show> root=query.from(Show.class);
			query.where(builder.equal(root.get("status"),"AVAILABLE"));
			List<Show> data = session.createQuery(query).getResultList();
			transaction.commit();
			session.clear();
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

    public static Show loadShowByID(Session session, int show_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Show> query = builder.createQuery(Show.class);
            Root<Show> root=query.from(Show.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("status"),"AVAILABLE");
            predicates[1]=builder.equal(root.get("ID"),show_id);
            query.where(predicates);
            Show data = session.createQuery(query).getResultList().get(0);
            transaction.commit();
            session.clear();
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

//            List<Show> data = ShowsController.loadShows(session);
//            for(int i=0;i< data.size();i++){
//                if(data.get(i).getHall()== show.getHall() && data.get(i).getDate() ==show.getDate()){
//                    return  false;
//                }
//            }
            if (!session.getTransaction().isActive()){
                Transaction transaction = session.beginTransaction();
                session.save(show);
                session.flush();


                transaction.commit();
                session.clear();
                return true;
            }
            return false;
            
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
            update_query.set("dateTime", newTime);
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

    public static List<Show> loadShowsByStartDate(Session session, LocalDateTime start) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Show> query = builder.createQuery(Show.class);
            Root<Show> root=query.from(Show.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("status"),"AVAILABLE");
            predicates[1]=builder.greaterThanOrEqualTo(root.get("dateTime"),start);
            query.where(predicates);
            List<Show> data = session.createQuery(query).getResultList();
            transaction.commit();
            session.clear();
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

    public static List<Show> loadShowsByEndDate(Session session, LocalDateTime endTime) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Show> query = builder.createQuery(Show.class);
            Root<Show> root=query.from(Show.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("status"),"AVAILABLE");
            predicates[1]=builder.lessThanOrEqualTo(root.get("dateTime"),endTime);
            query.where(predicates);
            List<Show> data = session.createQuery(query).getResultList();
            transaction.commit();
            session.clear();
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

}
