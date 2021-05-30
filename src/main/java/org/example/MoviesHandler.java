package org.example;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MoviesHandler {
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Movie.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
    public List<Movie> loadMovies(){
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
            query.from(Movie.class);
            //query.where(builder.equal(query,"Available"));
            List<Movie> data = session.createQuery(query).getResultList();
            session.getTransaction().commit();
            return (LinkedList<Movie>) data;

        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        } finally {
            session.close();
        }

    }

   public boolean addMovies(Movie newMovie) {
       try {
           SessionFactory sessionFactory = getSessionFactory();
           session = sessionFactory.openSession();
           session.beginTransaction();
           session.save(newMovie);
           session.flush();
           session.getTransaction().commit();
           return true;
           // Save everything.
       } catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
           System.err.println("An error occured, changes have been rolled back.");
           exception.printStackTrace();
           return false;
       } finally {
           session.close();
       }
   }
   public boolean deleteMovie(int movie_id){
       try {
           SessionFactory sessionFactory = getSessionFactory();
           session = sessionFactory.openSession();
           session.beginTransaction();

           CriteriaBuilder builder = session.getCriteriaBuilder();
           CriteriaUpdate<Movie> update_query=builder.createCriteriaUpdate(Movie.class);
           Root<Movie> root=update_query.from(Movie.class);
           update_query.set("status", "NOT_AVAILABLE");
           update_query.where(builder.equal(root.get("id"),movie_id));
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
       } finally {
           session.close();
       }
   }




}
