package org.example.Controllers;

import org.example.entities.Complaint;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.List;

public class ComplaintsController {

    public static List<Complaint> loadComplaints(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
            Root<Complaint> root=query.from(Complaint.class);
            query.where(builder.equal(root.get("active"),true));
            List<Complaint> data = session.createQuery(query).getResultList();
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

    public static boolean markComplaintAsDone(Session session, int complaint_id){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Complaint> update_query=builder.createCriteriaUpdate(Complaint.class);
            Root<Complaint> root=update_query.from(Complaint.class);
            update_query.set("handled", true);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"),complaint_id));
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
    public static boolean addComplaint(Session session, Complaint comp) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(comp);
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
    public static boolean deactivateAllComplaintsAfter24Hours(Session session){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Complaint> update_query=builder.createCriteriaUpdate(Complaint.class);
            Root<Complaint> root=update_query.from(Complaint.class);
            update_query.set("active", false);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("creationDate"), LocalDateTime.now().minusHours(24));
            predicates[1]=builder.equal(root.get("handled"),false);
            update_query.where();
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
    public static List<Complaint> makeComplaintsReportByMonth(Session session, Integer month, Integer year) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
            Root<Package> root=query.from(Package.class);
            Predicate[] predicates=new Predicate[2];
            //predicates[0]=builder.equal(root.get("active"),true);
            predicates[0]=builder.equal(builder.function("MONTH", Integer.class, root.get("creationDate")),month);
            predicates[1]=builder.equal(builder.function("YEAR", Integer.class, root.get("creationDate")),year);
            query.where(predicates);
            query.orderBy(builder.asc(root.get("creationDate")));
            List<Complaint> data = session.createQuery(query).getResultList();
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
