package org.example.Controllers;

import org.example.entities.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
    public static String loadCustomerMail(Session session,int customer_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> query = builder.createQuery(Customer.class);
            Root<Customer> root=query.from(Customer.class);
            query.where(builder.equal(root.get("id"),customer_id));
            Customer data = session.createQuery(query).getResultList().get(0);
            String email=data.getEmail();
            transaction.commit();
            return email;
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
