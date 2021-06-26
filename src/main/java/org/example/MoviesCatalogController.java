package org.example;

import java.util.List;

import org.example.entities.Movie;

public class MoviesCatalogController {
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

}}
