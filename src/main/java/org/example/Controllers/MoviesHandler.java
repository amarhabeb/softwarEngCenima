package org.example.Controllers;

import org.example.entities.Movie;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;


public class MoviesHandler {
    
    public static List<Movie> loadMovies(Session session){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
            query.from(Movie.class);
            List<Movie> data = session.createQuery(query).getResultList();
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

   public static boolean addMovies(Session session, Movie newMovie) {
       try {
           session.save(newMovie);
           session.flush();
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
   public static boolean deleteMovie(Session session, int movie_id){
       try {
           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaUpdate<Movie> update_query=builder.createCriteriaUpdate(Movie.class);
           Root<Movie> root=update_query.from(Movie.class);
           update_query.set("status", "NOT_AVAILABLE");
           update_query.where(builder.equal(root.get("ID"),movie_id));
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
}
