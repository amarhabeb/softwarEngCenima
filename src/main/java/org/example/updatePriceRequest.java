package org.example;

public class updatePriceRequest {
    private contentManager requestedBy;
    private double updatedPrice;
    private boolean checked;
    private boolean approved;

    public updatePriceRequest(contentManager requestedBy, double updatedPrice) {
        this.requestedBy = requestedBy;
        this.updatedPrice = updatedPrice;
        this.checked = false;
        this.approved = false;
    }

    public contentManager getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(contentManager requestedBy) {
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
