package org.example.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Timer;

@Entity
@Table(name ="complaint")

public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private int text;
    //private Timer timer = new Timer();
    private LocalDateTime creationDate;
    private boolean active;
    private boolean handled;

    public Complaint(int text) {
        this.text = text;
        this.creationDate= LocalDateTime.now();
        this.active = true;
        this.handled = false;
    }

    public Complaint(){}

    public void setText(int text) {
        this.text = text;
    }

    /*public void setTimer(Timer timer) {
        this.timer = timer;
    }*/

    public LocalDateTime getCreationDate() {
        return creationDate;
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
    /*
    public Timer getTimer() {
        return timer;
    }
*/
    public boolean isActive() {
        return active;
    }

    public boolean isHandled() {
        return handled;
    }
    public void deactivateComplaint(){
        this.active=false;
    }
}
