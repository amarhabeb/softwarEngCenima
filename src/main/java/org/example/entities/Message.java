package org.example.entities;

public abstract class Message {
    protected int ID;
    protected String text;

    public Message(int ID, String text){
        this.ID = ID;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getID() {
        return ID;
    }
}
