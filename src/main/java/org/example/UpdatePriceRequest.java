package org.example;

public class UpdatePriceRequest {
    private ContentManager requestedBy;
    private double updatedPrice;
    private boolean checked;
    private boolean approved;

    public UpdatePriceRequest(ContentManager requestedBy, double updatedPrice) {
        this.requestedBy = requestedBy;
        this.updatedPrice = updatedPrice;
        this.checked = false;
        this.approved = false;
    }

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
}
