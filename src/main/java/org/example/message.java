package org.example;

public abstract class message {
    protected int ID;
    protected String text;

    public message(int ID, String text){
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
