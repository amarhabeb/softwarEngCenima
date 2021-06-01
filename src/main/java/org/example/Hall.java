package org.example;

public class Hall {
    private int number;
    private int capacity;
    private int type;
    private int maxSeats;

    public Hall(int number, int capacity, int type, int maxSeats){
        this.number = number;
        this.capacity = capacity;
        this.type = type;
        this.maxSeats = maxSeats;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getType() {
        return type;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }

    public boolean isFull(){
        return capacity == maxSeats;
    }
}
