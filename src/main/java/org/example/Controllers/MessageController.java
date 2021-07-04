package org.example.Controllers;

import org.example.entities.Message;
import org.hibernate.Session;

public class MessageController {
    public static boolean addMessage(Session session, Message msg) {
        try {
            session.save(msg);
            session.flush();
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
}
