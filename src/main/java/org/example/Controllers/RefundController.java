package org.example.Controllers;


import org.example.entities.Refund;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RefundController {
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
}
