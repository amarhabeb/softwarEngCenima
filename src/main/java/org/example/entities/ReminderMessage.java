package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="remindermessage")

public class ReminderMessage extends Message{

    public ReminderMessage(){super("Your Link is about to be active in 1 hour. Stay Tuned!");}
}
