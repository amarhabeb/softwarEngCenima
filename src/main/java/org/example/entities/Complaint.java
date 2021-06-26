package org.example.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Timer;

public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private int text;
    private Timer timer = new Timer();
    private boolean active;
    private boolean handled;

    public Complaint(int text) {
        this.text = text;
        this.active = true;
        this.handled = false;
    }

    public Complaint(){}

    public void setText(int text) {
        this.text = text;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }


    public int getID() {
        return ID;
    }

    public int getText() {
        return text;
    }

    public Timer getTimer() {
        return timer;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isHandled() {
        return handled;
    }
}
