package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="regulations")

public class Regulations {
    private boolean status;	// will be false if there is no limitations
    private int Y;	// this is the same Y described in the requirements file
    public Regulations (boolean status, int Y){
        this.status = status;
        this.Y = Y;
    }

    public boolean getRegulations() {
        return status;
    }

    public void setRegulations(boolean status) {
        this.status = status;
    }

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}
}
