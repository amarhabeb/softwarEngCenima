package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="newmoviemessage")

public class NewMovieMessage extends Message{
    public NewMovieMessage(int ID, String text) {
        super(text);
    }
    public NewMovieMessage(){super();}
}
