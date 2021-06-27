package org.example.entities;

public class NewMovieMessage extends Message{
    public NewMovieMessage(int ID, String text) {
        super(text);
    }
    public NewMovieMessage(){super();}
}
