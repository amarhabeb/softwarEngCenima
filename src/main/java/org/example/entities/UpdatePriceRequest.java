package org.example.entities;

import org.example.ContentManager;

public class UpdatePriceRequest {
    private org.example.ContentManager requestedBy;
    int show_id;
    private double updatedPrice;
    private boolean checked;
    private boolean approved;

    public UpdatePriceRequest(org.example.ContentManager requestedBy, int show_id, double updatedPrice) {
        this.requestedBy = requestedBy;
        this.show_id = show_id;
        this.updatedPrice = updatedPrice;
        this.checked = false;
        this.approved = false;
    }

    public org.example.ContentManager getRequestedBy() {
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

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