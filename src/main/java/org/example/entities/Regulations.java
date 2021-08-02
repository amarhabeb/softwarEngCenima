package org.example.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name ="regulations")

public class Regulations implements Serializable {
    
	@Id
	private int reg_id;
    private boolean status;	// will be false if there is no limitations
    private int Y;	// this is the same Y described in the requirements file
    public Regulations (boolean status, int Y){
        this.status = status;
        this.Y = Y;
    }

    public Regulations() {
        status=false;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getReg_id() {
		return reg_id;
	}

	public void setReg_id(int reg_id) {
		this.reg_id = reg_id;
	}
}
