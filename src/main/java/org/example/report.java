package org.example;

 abstract class report {
    protected int ID;
    protected String date;


    public report(int ID, String date) {
        this.ID = ID;
        this.date = date;
    }

     public report() {

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
