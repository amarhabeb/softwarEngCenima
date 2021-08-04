package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;

@Entity
@Table(name ="complaint")

public class Complaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private int order_id;
    private String text;
    //private Timer timer = new Timer();
    private LocalDateTime creationDate;
    private boolean active;
    private boolean handled;

    public Complaint(String text, int order_id) {
        this.text = text;
        this.creationDate= LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        this.active = true;
        this.handled = false;
        this.order_id = order_id;
    }

    public Complaint(){}

    public void setText(String text) {
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

    public String getText() {
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

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
