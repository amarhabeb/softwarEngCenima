package org.example.entities;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.awt.*;
import java.lang.Math;
import java.util.List;


public class Hall {
    private int number;
    private int capacity;	// this is X in the requirements file
    private int type;
    private int maxSeats;
    @OneToMany(targetEntity = Seat.class)
    private List<Seat> seats;
    @ManyToOne(targetEntity = Cinema.class)
    private Cinema cinema;

    public Hall(){}

    public Hall(int number, int capacity, int type, int maxSeats, List<Seat> seats, Cinema cinema) {
        this.number = number;
        this.capacity = capacity;
        this.type = type;
        this.maxSeats = maxSeats;
        this.seats = seats;
        this.cinema=cinema;
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

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public void addSeat(Seat s){
        seats.add(s);
    }
    public void removeSeat(Seat s){
        seats.remove(s);
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public boolean isFull(){
        return capacity == maxSeats;
    }

    ///////// WILL BE CHANGED //////////
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
