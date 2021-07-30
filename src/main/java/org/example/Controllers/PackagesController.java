package org.example.Controllers;

import org.example.entities.PackageOrder;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.time.Month;
import java.time.Year;
import java.util.List;

public class PackagesController {
    public static List<PackageOrder> loadPackages(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<PackageOrder> query = builder.createQuery(PackageOrder.class);
            query.from(PackageOrder.class);
            List<PackageOrder> data = session.createQuery(query).getResultList();
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
    public static List<PackageOrder> loadCustomersPackages(Session session, int cust_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<PackageOrder> query = builder.createQuery(PackageOrder.class);
            Root<PackageOrder> root=query.from(PackageOrder.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.equal(root.get("customer_id"),cust_id);
            predicates[1]=builder.equal(root.get("status"),true);
            predicates[2]=builder.equal(root.get("active"),true);
            query.where(predicates);
            List<PackageOrder> data = session.createQuery(query).getResultList();
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
    public static Boolean addPackage(Session session,PackageOrder newPackage) throws Exception {
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
            CriteriaQuery<PackageOrder> query = builder.createQuery(PackageOrder.class);
            Root<PackageOrder> root=query.from(PackageOrder.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("ID"),package_id);
            predicates[1]=builder.equal(root.get("active"),true);
            query.where(predicates);
            List<PackageOrder> data = session.createQuery(query).getResultList();
            int res=data.get(0).getCounter();
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
    public static List<PackageOrder> makePackagesReportByMonth(Session session, Month month, Year year) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<PackageOrder> query = builder.createQuery(PackageOrder.class);
            Root<PackageOrder> root=query.from(PackageOrder.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.equal(root.get("active"),true);
            predicates[1]=builder.equal(builder.function("MONTH", Integer.class, root.get("orderDate")),month);
            predicates[2]=builder.equal(builder.function("YEAR", Integer.class, root.get("orderDate")),year);
            query.where(predicates);
            query.orderBy(builder.asc(root.get("orderDate")));
            List<PackageOrder> data = session.createQuery(query).getResultList();
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


