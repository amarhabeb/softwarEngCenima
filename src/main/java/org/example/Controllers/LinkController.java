package org.example.Controllers;

import org.example.entities.Link;


import org.example.entities.Refund;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LinkController {
    public static List<Link> loadLinks(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Link> query = builder.createQuery(Link.class);
            Root<Link> root=query.from(Link.class);
            query.where(builder.equal(root.get("status"),true));
            List<Link> data = session.createQuery(query).getResultList();
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
    public static List<Link> loadCustomerLinks(Session session, int customer_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Link> query = builder.createQuery(Link.class);
            Root<Link> root=query.from(Link.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("customer_id"),customer_id);
            //predicates[1]=builder.equal(root.get("status"),true);
            predicates[1]=builder.equal(root.get("active"),true);
            query.where(predicates);
            List<Link> data = session.createQuery(query).getResultList();
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
    public static Boolean addLink(Session session,Link newLink) throws Exception {
        try {
            Transaction transaction = session.beginTransaction();
            session.save(newLink);
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
    public static LocalDateTime loadLinkTime(Session session, int link_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Link> query = builder.createQuery(Link.class);
            Root<Link> root=query.from(Link.class);
            query.where(builder.equal(root.get("ID"),link_id));
            List<Link> data = session.createQuery(query).getResultList();
            LocalDateTime time=data.get(0).getFromTime();
            transaction.commit();
            return time;
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }
    }
    public static double loadLinkPrice(Session session, int link_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Link> query = builder.createQuery(Link.class);
            Root<Link> root=query.from(Link.class);
            query.where(builder.equal(root.get("ID"),link_id));
            List<Link> data = session.createQuery(query).getResultList();
            double price=data.get(0).getPrice();
            transaction.commit();
            return price;
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return -1;
        }
    }
    public static boolean cancelLink(Session session, int link_id) {
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Link> update_query = builder.createCriteriaUpdate(Link.class);
            Root<Link> root = update_query.from(Link.class);
            update_query.set("status", false);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"), link_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            transaction.commit();
            session.clear();
            return true;
        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }

    public static boolean deactivateLinksWhenTimePassed(Session session){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Link> update_query=builder.createCriteriaUpdate(Link.class);
            Root<Link> root=update_query.from(Link.class);
            update_query.set("active", false);
            update_query.where(builder.lessThan(root.<LocalDateTime>get("toTime"), LocalDateTime.now()));
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
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }


    public static boolean activateLinksWhenTimeCome(Session session){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Link> update_query=builder.createCriteriaUpdate(Link.class);
            Root<Link> root=update_query.from(Link.class);
            update_query.set("active", true);
            update_query.where(builder.lessThan(root.<LocalDateTime>get("fromTime"), LocalDateTime.now()));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            session.clear();
            transaction.commit();
            session.clear();
            deactivateLinksWhenTimePassed(session);
            return true;
            // Save everything.
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }



    public static List<Link> makeLinksReportByMonth(Session session, Month month, Year year) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Link> query = builder.createQuery(Link.class);
            Root<Link> root=query.from(Link.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.equal(root.get("active"),true);
            predicates[1]=builder.equal(builder.function("MONTH", Integer.class, root.get("orderDate")),month);
            predicates[2]=builder.equal(builder.function("YEAR", Integer.class, root.get("orderDate")),year);
            query.where(predicates);
            query.orderBy(builder.asc(root.get("orderDate")));
            List<Link> data = session.createQuery(query).getResultList();
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
