package org.example.entities;

import org.example.entities.Message;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="linkmessage")

public class LinkMessage extends Message {
    public LinkMessage(int ID, String text) {
        super(text);
    }
    public LinkMessage(){super();}
}
