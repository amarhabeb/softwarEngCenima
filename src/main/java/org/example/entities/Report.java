package org.example.entities;

 abstract class Report {
    protected int ID;
    protected String date;


    public Report(int ID, String date) {
        this.ID = ID;
        this.date = date;
    }

     public Report() {

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
}
