package org.example;
import java.lang.Math;


public class Hall {
    private int number;
    private int capacity;	// this is X in the requirements file
    private int type;
    private int maxSeats;
    

    public Hall(int number, int capacity, int type, Regulations reg){
        this.number = number;
        this.capacity = capacity;
        this.type = type;
        this.maxSeats = calculateMaxSeats(capacity, reg);
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
    
    // a function to calculate the maxSeats of a hall considering the current regulations
    private int calculateMaxSeats(int capacity, Regulations reg) {
    	if (reg.getRegulations() == false) {
    		return capacity;
    	}
    	int Y = reg.getY();
    	if(1.2*Y < capacity) {
    		return Y;
    	}
    	else{
    		if(0.8*Y < capacity) {
    			return (int) Math.round(0.8*Y);
    		}
    		else {
    			return (int) Math.round(0.5*capacity);
    		}
    	}
    }
}
