package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name ="seat")

public class Seat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private boolean available;
    private int number; //I guess there's no need for this field,
                        // we can give the index of each seat in Seats[] as its number
    private int line; // no need as above reason
    @ManyToOne(targetEntity = Hall.class, cascade = CascadeType.ALL)
    private Hall hall;
    private boolean active;

    public Seat(boolean available, int number, int line,Hall hall) {
        this.available = available;
        this.number = number;
        this.line = line;
        this.hall=hall;
        this.active=true;
    }
    public Seat(){}
    public boolean isAvailable() {
        return available;
    }
    
    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }

    public int getID() {
        return ID;
    }
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public void setID(int iD) {
		ID = iD;
	}
}
