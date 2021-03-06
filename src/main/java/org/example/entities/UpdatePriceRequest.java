package org.example.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name ="updatepricerequest")

public class UpdatePriceRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    //@OneToOne(targetEntity = ContentManager.class, cascade = CascadeType.ALL)
    //private ContentManager requestedBy;
    private Integer requestedBy_id;
    private int show_id;
    private double updatedPrice;
    //private boolean checked;
    private boolean approved;
    private boolean active;

    public UpdatePriceRequest(int requestedBy, int show_id, double updatedPrice) {
        this.requestedBy_id = requestedBy;
        this.show_id = show_id;
        this.updatedPrice = updatedPrice;
        this.approved = false;
        this.active=true;
    }
    public UpdatePriceRequest(){
    	this.approved = false;
        this.active=true;
    }

    public Integer getRequestedBy_id() {
        return requestedBy_id;
    }

    public void setRequestedBy_id(int requestedBy_id) {
        this.requestedBy_id = requestedBy_id;
    }

    public double getUpdatedPrice() {
        return updatedPrice;
    }

    public void setUpdatedPrice(double updatedPrice) {
        this.updatedPrice = updatedPrice;
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
    /*public boolean isChecked() {
        return checked;
    }*/

    /*public void setChecked(boolean checked) {
        this.checked = checked;
    }*/

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

	public int getShow_id() {
		return show_id;
	}

	public void setShow_id(int show_id) {
		this.show_id = show_id;
	}
	public void setID(int iD) {
		ID = iD;
	}
}
