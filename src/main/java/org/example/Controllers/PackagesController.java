package org.example.Controllers;


import org.example.entities.Customer;
import org.example.entities.Movie;
import org.example.entities.Package;
import org.example.entities.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.Queue;

public class PackagesController {
    public static List<Package> loadPackages(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Package> query = builder.createQuery(Package.class);
            query.from(Package.class);
            List<Package> data = session.createQuery(query).getResultList();
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
    public static List<Package> loadCustomersPackages(Session session, int cust_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Package> query = builder.createQuery(Package.class);
            Root<Package> TicketRoot = query.from(Package.class);
            Join<Package, Payment> payment = TicketRoot.join("payment");
            Join<Payment, Customer> customerid = TicketRoot.join("id");
            query.from(Package.class);
            query.select(TicketRoot).where(builder.equal(customerid.get("id"),cust_id));
            List<Package> data = session.createQuery(query).getResultList();
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
    public static Boolean addPackage(Package newPackage, Session session) throws Exception {
        try {
            Transaction transaction = session.beginTransaction();
            session.save(newPackage);
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
}


