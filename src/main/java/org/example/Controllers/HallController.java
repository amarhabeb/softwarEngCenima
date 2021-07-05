package org.example.Controllers;

import org.example.entities.Hall;
import org.example.entities.Regulations;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

public class HallController {
    public static List<Hall> loadHalls(Session session) throws Exception {
        try {
            Transaction transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Hall> query = builder.createQuery(Hall.class);
            Root<Hall> root = query.from(Hall.class);
            query.where(builder.equal(root.get("active"), true));
            List<Hall> data = session.createQuery(query).getResultList();
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

    public static boolean addHall(Session session, Hall hall) throws Exception {
        try {
            Transaction transaction = session.beginTransaction();
            session.save(hall);
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

    public static boolean deleteHall(Session session, int hall_id) {
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Hall> update_query = builder.createCriteriaUpdate(Hall.class);
            Root<Hall> root = update_query.from(Hall.class);
            update_query.set("active", false);
            update_query.where(builder.equal(root.get("ID"), hall_id));
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

    public static Boolean calcMaxSeats(Session session) throws Exception {
        try {
            List<Regulations> reg = RegulationsController.loadReagulations(session);
            Regulations reg1 = reg.get(0);

            List<Hall> halls = HallController.loadHalls(session);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaUpdate<Hall> update_query = builder.createCriteriaUpdate(Hall.class);
            Root<Hall> root = update_query.from(Hall.class);
            for (int i = 0; i < halls.size(); i++) {
                Hall hall = halls.get(i);
                if (reg1.getRegulations() == true) {
                    int Y = reg1.getY();
                    if (1.2 * Y < hall.getCapacity()) {
                        update_query.set("maxSeats", Y);
                        update_query.where(builder.equal(root.get("ID"), hall.getID()));
                        Transaction transaction = session.beginTransaction();
                        session.createQuery(update_query).executeUpdate();
                        transaction.commit();
                        //session.clear();
                        return true;
                    } else {
                        if (0.8 * Y < hall.getCapacity()) {
                            update_query.set("maxSeats", (int) Math.round(0.8 * Y));
                            update_query.where(builder.equal(root.get("ID"), hall.getID()));
                            Transaction transaction = session.beginTransaction();
                            session.createQuery(update_query).executeUpdate();
                            transaction.commit();
                            //session.clear();
                            return true;
                        } else {

                            update_query.set("maxSeats", (int) Math.round(0.5 * hall.getCapacity()));
                            update_query.where(builder.equal(root.get("ID"), hall.getID()));
                            Transaction transaction = session.beginTransaction();
                            session.createQuery(update_query).executeUpdate();
                            transaction.commit();
                            //session.clear();
                            return true;
                        }


                    }

                } else {
                    update_query.set("maxSeats", hall.getCapacity());
                    update_query.where(builder.equal(root.get("ID"), hall.getID()));
                    Transaction transaction = session.beginTransaction();
                    session.createQuery(update_query).executeUpdate();
                    transaction.commit();
                    //session.clear();
                    return true;
                }

            }


        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }
        return false;

    }
}

