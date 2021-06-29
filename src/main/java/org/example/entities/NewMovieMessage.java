package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="newmoviemessage")

public class NewMovieMessage extends Message{
    public NewMovieMessage(){super("New movie launched today waiting for you to watch!!");}
}
