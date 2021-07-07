package org.example.Controllers;



import org.example.entities.Refund;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;

public class RefundController {

    public static List<Refund> loadRefunds(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Refund> query = builder.createQuery(Refund.class);
            query.from(Refund.class);
            List<Refund> data = session.createQuery(query).getResultList();
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
    public static boolean addRefund(Session session, Refund refund) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(refund);
            session.flush();
            transaction.commit();
            return true;
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
    }
    /// NOTE: can make report for cinema.. Requirements file is NOT clear..
    public static List<Refund> makeRefundsReportByMonth(Session session, Month month, Year year) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Refund> query = builder.createQuery(Refund.class);
            Root<Refund> root=query.from(Refund.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.notEqual(root.get("order_id"),-1);
            predicates[1]=builder.equal(builder.function("MONTH", Integer.class, root.get("date")),month);
            predicates[2]=builder.equal(builder.function("YEAR", Integer.class, root.get("date")),year);
            query.where(predicates);
            query.orderBy(builder.asc(root.get("date")));
            List<Refund> data = session.createQuery(query).getResultList();
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
