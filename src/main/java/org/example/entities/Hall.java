package org.example.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name ="hall")
public class Hall implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private int number;
    private int capacity;	// this is X in the requirements file
    private int type;
    private int maxSeats;
    @OneToMany(targetEntity = Seat.class, cascade = CascadeType.ALL)
    private List<Seat> seats;
        @ManyToOne(targetEntity = Cinema.class, cascade = CascadeType.ALL)
    private Cinema cinema;
    private boolean active;

    //@OneToMany (targetEntity = Show.class, cascade = CascadeType.ALL)
    //private List<Show> showss;

    public Hall(){
    	this.active = true;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

   /* public void setShows(List<Show> shows) {
        this.showss = shows;
    }
    public void addShow(Show show){
        this.showss.add(show);

    }*/

    public Hall(int number, int capacity,List<Seat> seats,  Cinema cinema){//,List<Show> shows) {

        this.number = number;
        this.capacity = capacity;
        this.type = 1;
        this.maxSeats = capacity;
        this.cinema=cinema;
        this.active=true;
        //this.showss=shows;

//        List<Seat> tempSeats = new LinkedList<Seat>();
//        for (int i=1; i<=capacity; i++){
//            Seat seat = new Seat(true, i%10, i/10 +1, this);/*every line has 10 seats*/
//            tempSeats.add(seat);
//        }
        this.seats = seats;


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

    public int getID() {
        return ID;
    }




    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public boolean isFull(){
        return capacity == maxSeats;
    }


	public boolean isActive() {
		return active;
	}


	
	@Override
	public String toString() {
		return Integer.toString(number);
	}

    ///////// WILL BE CHANGED //////////
    // a function to calculate the maxSeats of a hall considering the current regulations
//    public void setMaxSeats(Regulations reg) {
//    	if (reg.getRegulations() == false) {
//    		this.maxSeats=capacity;
//    	}
//    	else {
//            int Y = reg.getY();
//            if (1.2 * Y < capacity) {
//                this.maxSeats = Y;
//            } else {
//                if (0.8 * Y < capacity) {
//                    this.maxSeats = (int) Math.round(0.8 * Y);
//                } else {
//                    this.maxSeats = (int) Math.round(0.5 * capacity);
//                }
//            }
//        }
//    }

}
