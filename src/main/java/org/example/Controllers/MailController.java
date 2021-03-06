package org.example.Controllers;
import org.example.entities.Customer;
import org.example.entities.Link;
import org.example.entities.Movie;
import org.example.entities.PackageOrder;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailController{
    public static void sendMail(String mesasge1, String mail, String topic){
        // Recipient's email ID needs to be mentioned.
        String to = mail;

        // Sender's email ID needs to be mentioned
        String from = "softwareengp@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("softwareengp@gmail.com", "Se123123");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(topic);

            // Now set the actual message
            message.setText(mesasge1);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    public static boolean sendNewMoviesMail(org.hibernate.Session session){
        try{
            List<Movie> newMovies=MoviesController.loadNewMovies(session);
            String newMoviesText="";
            for(Movie m: newMovies){
                newMoviesText+="Name: "+ m.getName_en() +"\n"+
                               "Summary: " +m.getSummary()+"\n"+
                                "Director: " +m.getDirector()+"\n"+
                                "Launch Date: " +m.getLanuch_date().toString()+"\n"+
                                "*********************"+"\n\n\n";
            }
            if(newMovies!=null){
                List<PackageOrder> packageOrderList=PackagesController.loadValidPackages(session);
                if (!packageOrderList.isEmpty())
                for(PackageOrder pac: packageOrderList) {

                        String email = CustomerController.loadCustomerMail(session, pac.getCusomer_id());
                        if (email != null)
                            sendMail(newMoviesText, email, "Don't Miss! New Movies Coming Soon");
                    }
                    return true;
                }

            return false;

        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }

    }

    public static boolean sendReminderLink(org.hibernate.Session session){
        try{
            List<Link> LinksToremind=LinkController.LoadLinksToRemind(session);
            String linkReminderTxt="";
            for(Link L: LinksToremind){
                linkReminderTxt+="Link: "+ L.getLink() +"\n"+
                        "You can start watching from: " +L.getFromTime().truncatedTo(ChronoUnit.MINUTES)+"\n" +
                        "until: " +L.getToTime().truncatedTo(ChronoUnit.MINUTES)+"."+"\n"+
                        "ENJOY!! \n" +
                        "*********************"+"\n\n\n";
            }
            if(LinksToremind!=null){

                for(Link link: LinksToremind) {


                    String email = CustomerController.loadCustomerMail(session, link.getCusomer_id());
                    if (email != null)
                        sendMail(linkReminderTxt, email, "Don't Miss! Your Movie Will Be Online After Only One Hour");
                }
                return true;
            }

            return false;

        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            return false;
        }

    }


}