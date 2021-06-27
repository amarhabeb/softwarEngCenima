package org.example.entities;

import javax.persistence.ManyToOne;

public class Seat {
    private boolean available;
    private int number; //I guess there's no need for this field,
                        // we can give the index of each seat in Seats[] as its number
    private int line; // no need as above reason
    @ManyToOne(targetEntity = Hall.class)
    private Hall hall;

    public Seat(boolean available, int number, int line, Hall hall) {
        this.available = available;
        this.number = number;
        this.line = line;
        this.hall=hall;
    }
    public Seat(){}
    public boolean isAvailable() {
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
}
