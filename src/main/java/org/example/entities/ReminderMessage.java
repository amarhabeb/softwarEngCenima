package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="remindermessage")

public class ReminderMessage extends Message{

    public ReminderMessage(int ID, String text) {
        super(text);
    }
    public ReminderMessage(){super();}
}
