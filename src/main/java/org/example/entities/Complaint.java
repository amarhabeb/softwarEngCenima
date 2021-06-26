package org.example.entities;

import java.util.Timer;

public class Complaint {
    private int ID;
    private int text;
    private Timer timer = new Timer();
    private boolean active;
    private boolean handled;

    public Complaint(int id, int text) {
        ID = id;
        this.text = text;
        this.active = true;
        this.handled = false;
    }

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
