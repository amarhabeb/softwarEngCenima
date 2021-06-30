package org.example.Controllers;

import org.example.entities.Employee;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.*;
import java.util.List;

public class EmployeeController {
    public static List<Employee> loadEmployees(Session session) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
            query.from(Employee.class);
            List<Employee> data = session.createQuery(query).getResultList();
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

    public static boolean addEmployee(Session session, Employee emp) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            session.save(emp);
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

    public static boolean deleteEmployee(Session session, int emp_id){
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Employee> update_query = builder.createCriteriaUpdate(Employee.class);
            Root<Employee> root=update_query.from(Employee.class);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"),emp_id));
            Transaction transaction = session.beginTransaction();
            session.createQuery(update_query).executeUpdate();
            transaction.commit();
            //session.clear();
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
    //return NULL if details is incorrect
    public static Employee logIn(Session session, String username, String password) throws Exception{
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
            Root<Employee> root=query.from(Employee.class);
            Predicate[] predicates = new Predicate[2];
            predicates[0]=builder.equal(root.get("username"),username);
            predicates[1]=builder.equal(root.get("password"),password);
            query.where(predicates);
            Employee data = session.createQuery(query).uniqueResult();
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
