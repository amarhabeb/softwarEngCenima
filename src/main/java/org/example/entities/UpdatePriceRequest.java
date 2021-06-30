package org.example.entities;
import javax.persistence.*;

@Entity
@Table(name ="updatepricerequest")

public class UpdatePriceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;
    private ContentManager requestedBy;
    int show_id;
    private double updatedPrice;
    //private boolean checked;
    private boolean approved;
    private boolean active;

    public UpdatePriceRequest(ContentManager requestedBy, int show_id, double updatedPrice) {
        this.requestedBy = requestedBy;
        this.show_id = show_id;
        this.updatedPrice = updatedPrice;
        //this.checked = false;
        this.approved = false;
        this.active=true;
    }
    public UpdatePriceRequest(){}

    public ContentManager getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(ContentManager requestedBy) {
        this.requestedBy = requestedBy;
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
}
