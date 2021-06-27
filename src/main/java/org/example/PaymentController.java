package org.example;

import org.example.entities.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PaymentController {
    public static boolean makePayment(Session session, Payment payment) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(payment);
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
