package org.example.Controllers;


import org.example.entities.Order;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.List;

public class OrderController {
    @SuppressWarnings("exports")
    public  static List<Order> loadOrders(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root =query.from(Order.class);
            query.where(builder.equal(root.get("active"), true));
            List<Order> data = session.createQuery(query).getResultList();
            transaction.commit();
            return data;

        }
         catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }

    }

    public  static Order loadOrderByID(Session session,int order_id) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root =query.from(Order.class);
            Predicate[] predicates=new Predicate[2];
            predicates[0]=builder.equal(root.get("active"), true);
            predicates[1]=builder.equal(root.get("ID"), order_id);
            query.where(predicates);
            Order data = session.createQuery(query).getResultList().get(0);
            transaction.commit();
            return data;

        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }

    }
    public static List<Order> loadCutomersOrders(int cust_id,Session session) throws  Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root=query.from(Order.class);
            Predicate[] predicates=new Predicate[3];
            predicates[0]=builder.equal(root.get("customer_id"),cust_id);
            predicates[1]=builder.equal(root.get("status"),true);
            predicates[2]=builder.equal(root.get("active"),true);
            query.where(predicates);
            List<Order> data = session.createQuery(query).getResultList();
            transaction.commit();
            return data;
        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return null;
        }

    }
    public static Boolean addOrder(Order order, Session session ) throws Exception{
        try {

            Transaction transaction = session.beginTransaction();
            session.save(order);
            session.flush();
            transaction.commit();
            session.clear();
            return true;
        }catch (Exception exception){
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return false;

        }


    }
    public static Boolean removeOrder (int order_id, Session session)throws Exception{
        try{

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Order> query = builder.createCriteriaUpdate(Order.class);
            Root <Order> root = query.from(Order.class);
            query.set("status",false);
            Transaction transaction = session.beginTransaction();
            session.createQuery(query).executeUpdate();
            session.clear();
            transaction.commit();
            return  true;
        }catch (Exception exception){
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }




    }


}
