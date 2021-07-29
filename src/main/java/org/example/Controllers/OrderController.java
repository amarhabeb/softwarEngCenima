package org.example.Controllers;


import org.example.entities.Order;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
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
    public static List<Order> loadCutomersOrders(int cost_id,Session session) throws  Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();


            CriteriaQuery<Order> query = builder.createQuery(Order.class);
            Root<Order> root=query.from(Order.class);
            query.from(Order.class);
            query.where(builder.equal(root.get("price"),cost_id));
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
