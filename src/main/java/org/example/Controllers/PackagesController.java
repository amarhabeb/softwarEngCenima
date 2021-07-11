package org.example.Controllers;


import org.example.entities.Customer;
import org.example.entities.Package;
import org.example.entities.Payment;
import org.example.entities.Ticket;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.time.Month;
import java.time.Year;
import java.util.List;

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
    //get package_id from customer, and give them how many tickets left in their package
    public static int getNumberOfTicketsLeft(Session session, int package_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Package> query = builder.createQuery(Package.class);
            Root<Package> root=query.from(Package.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("ID"),package_id);
            predicates[1]=builder.equal(root.get("active"),true);
            query.where(predicates);
            List<Package> data = session.createQuery(query).getResultList();
            List<Ticket> tickets=data.get(0).getTickets();
            int res =0;
            for(int i=0;i< tickets.size();i++){
                if(tickets.get(i).isActive()){
                    res++;
                }
            }
            transaction.commit();
            return res;
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return -1;
        }
    }
    public static List<Package> makePackagesReportByMonth(Session session, Month month, Year year) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Package> query = builder.createQuery(Package.class);
            Root<Package> root=query.from(Package.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.equal(root.get("active"),true);
            predicates[1]=builder.equal(builder.function("MONTH", Integer.class, root.get("orderDate")),month);
            predicates[2]=builder.equal(builder.function("YEAR", Integer.class, root.get("orderDate")),year);
            query.where(predicates);
            query.orderBy(builder.asc(root.get("orderDate")));
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
}


