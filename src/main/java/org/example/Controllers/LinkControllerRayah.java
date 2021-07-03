package org.example.Controllers;

import org.example.entities.Customer;
import org.example.entities.Link;
import org.example.entities.Payment;
import org.example.entities.Refund;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.lang.Package;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LinkControllerRayah {
    public static List<Link> loadLinks(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Link> query = builder.createQuery(Link.class);
            query.from(Link.class);
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
            Root<Link> LinkRoot = query.from(Link.class);
            Join<Link, Payment> payment = LinkRoot.join("payment");
            Join<Payment, Customer> customerid = LinkRoot.join("id");
            query.from(Link.class);
            query.select(LinkRoot).where(builder.equal(customerid.get("id"),customer_id));
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
    public static Boolean addLink(Package newLink, Session session) throws Exception {
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
    public static Refund cancelLink(Session session, int link_id) {
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Link> update_query = builder.createCriteriaUpdate(Link.class);
            Root<Link> root = update_query.from(Link.class);
            update_query.set("status", false);
            update_query.where(builder.equal(root.get("ID"), link_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            session.clear();
            LocalDateTime DT = loadLinkTime(session, link_id);
            if (ChronoUnit.HOURS.between(LocalDateTime.now(),DT) >1){
                double price=loadLinkPrice(session,link_id);
                Refund refund=new Refund(price*0.5, link_id,LocalDateTime.now());
                boolean answer= RefundController.addRefund(session,refund);
                transaction.commit();
                session.clear();
                if(answer){
                    return refund;
                }
                else{
                    return null;
                }
            }
            else{
                return null;
            }
        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }
    }

}
