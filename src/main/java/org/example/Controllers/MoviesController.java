package org.example.Controllers;

import org.example.entities.Movie;
import org.example.entities.Show;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;


public class MoviesController {
    
    public static List<Movie> loadMovies(Session session){
        try {
        	Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
            Root<Movie> root=query.from(Movie.class);
            query.where(builder.equal(root.get("status"),"AVAILABLE"));
            List<Movie> data = session.createQuery(query).getResultList();
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

    public static List<Movie> loadOnlineMovies(Session session){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
            Root<Movie> root=query.from(Movie.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("status"),"AVAILABLE");
            predicates[1]=builder.equal(root.get("availableOnline"),true);
            query.where(predicates);
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

   public static boolean addMovie(Session session, Movie newMovie) {
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
    public static boolean setOnlineMovieOFF(Session session, int movie_id){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Movie> update_query=builder.createCriteriaUpdate(Movie.class);
            Root<Movie> root=update_query.from(Movie.class);
            update_query.set("availableOnline", false);
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
    public static boolean setOnlineMovieON(Session session, int movie_id){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Movie> update_query=builder.createCriteriaUpdate(Movie.class);
            Root<Movie> root=update_query.from(Movie.class);
            update_query.set("availableOnline", true);
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
    public static List<Movie> loadNewMovies(Session session) throws Exception {
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
            Root<Movie> root=query.from(Movie.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.equal(root.get("status"),"AVAILABLE");
            predicates[1]=builder.greaterThanOrEqualTo(root.get("launch_date"),LocalDate.now());
            predicates[2]=builder.lessThan(root.get("launch_date"),LocalDate.now().plusDays(7));
            query.where(predicates);
            List<Movie> data=session.createQuery(query).getResultList();
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

    public static List<Show> loadMovieShows(Session session,int movie_id){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
            Root<Movie> root=query.from(Movie.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("status"),"AVAILABLE");
            predicates[1]=builder.equal(root.get("id"),movie_id);
            query.where(predicates);
            List<Show> data = session.createQuery(query).getResultList().get(0).getShows();
            for(Show show : data){
                if(show.getStatus()!="AVAILABLE")
                    data.remove(show);
            }
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

}
