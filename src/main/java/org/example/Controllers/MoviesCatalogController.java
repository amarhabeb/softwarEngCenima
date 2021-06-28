package org.example.Controllers;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.example.entities.Movie;
import org.hibernate.Session;

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
