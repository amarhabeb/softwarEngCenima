package org.example.entities;

import javax.persistence.ManyToOne;

public class Seat {
    private boolean available;
    private int number; //I guess there's no need for this field,
                        // we can give the index of each seat in Seats[] as its number
    private int line; // no need as above reason

    public Seat(boolean available, int number, int line) {
        this.available = available;
        this.number = number;
        this.line = line;
    }

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

}
