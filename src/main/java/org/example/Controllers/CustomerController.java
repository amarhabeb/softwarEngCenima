package org.example.Controllers;

import org.example.entities.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CustomerController {
    public static boolean addCustomer(Session session, Customer customer) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(customer);
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
