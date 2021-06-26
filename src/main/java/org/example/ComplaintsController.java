package org.example;

import org.example.entities.Complaint;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

public class ComplaintsController {

    public static List<Complaint> loadComplaints(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Complaint> query = builder.createQuery(Complaint.class);
            query.from(Complaint.class);
            List<Complaint> data = session.createQuery(query).getResultList();
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

    public static boolean markComplaintAsDone(Session session, int complaint_id){
        try {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Complaint> update_query=builder.createCriteriaUpdate(Complaint.class);
            Root<Complaint> root=update_query.from(Complaint.class);
            update_query.set("handled", true);
            update_query.set("actice", false);
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
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }
}
