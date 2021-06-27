package org.example.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="message")

public abstract class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int ID;
    protected String text;

    public Message(String text){
        this.text = text;
    }
    public Message(){}

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
