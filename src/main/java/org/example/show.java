package org.example;

public class show {
    private int ID;
    private String date;
    private String time;
    private boolean isOnline;
    private boolean status;
    private double price;

    public show(int ID, String date, String time, boolean isOnline, boolean status, double price) {
        this.ID = ID;
        this.date = date;
        this.time = time;
        this.isOnline = isOnline;
        this.status = status;
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //    public boolean soldOut(){
//
//    }

//    public boolean isSoon(){
//
//    }
}
