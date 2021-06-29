package org.example;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.example.entities.Link;
import org.example.entities.LinkMessage;
import org.example.entities.Message;
import org.example.entities.Person;
import org.example.entities.ReminderMessage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.entities.Order;
import org.example.entities.Payment;
import org.example.entities.*;
public class LinksController {
	
	
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
	            System.err.println("An error occured, changes have been rolled back.");
	            exception.printStackTrace();
	            return null;
	        }
	    }
	 
	 public static List<Link> loadCostumersLinks(int cost_id,Session session) throws Exception{
	 try{
		 
		 Transaction transaction = session.beginTransaction();
         CriteriaBuilder builder = session.getCriteriaBuilder();
         CriteriaQuery<Link> query = builder.createQuery(Link.class);
         query.from(Ticket.class);
         List<Link> data = session.createQuery(query).getResultList();
         Root<Link> LinkRoot = query.from(Link.class);
         
         Join<Customer, Payment> payment = LinkRoot.join("payment");
         Join<Payment,Link> customerid = LinkRoot.join("id");
         query.select(LinkRoot).where(builder.equal(customerid.get("id"),cost_id));
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
	 


	/*public boolean sendLinkAsMessage(LinkMessage message,Session session) {
		
		
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Message> personCriteria =builder.createQuery(Message.class);
		Root<Message> personRoot = Message.from( Message.class );
		
		Join<LinkMessage,Link> LinkMessageLink = personRoot.join(LinkMessage._Link);
		Join<Link,Order> LinkOrder = LinkMessageLink.join(Link._Order);
		Join<Order,Payment> OrderPayment = LinkOrder.join(Order_.Payment);
		Join<Payment,Person> PaymentPerson = OrderPayment.join(Payment_.Person);
PaymentPerson.get("id");

//we also need to add sending the message 

		
		
	
		return false;
	}

	
	
	public boolean sendReminder(ReminderMessage message,Session session) {
		
		

		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Message> personCriteria =builder.createQuery(Message.class);
		Root<Message> personRoot = Message.from( Message.class );
		
		Join<ReminderMessage,Link> ReminderMessageLink = personRoot.join(LinkMessage._Link);
		Join<Link,Order> LinkOrder = ReminderMessageLink.join(Link._Order);
		Join<Order,Payment> OrderPayment = LinkOrder.join(Order_.Payment);
		Join<Payment,Person> PaymentPerson = OrderPayment.join(Payment_.Person);
		
		
		
		return false;
			
	}

}*/}