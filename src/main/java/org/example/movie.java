package org.example;


public class movie {
    private int ID;
    private String name;
    private String hebrewName;
    private String director;
    private String[] actors;
    private String summary;
    private Image image;
    private String launchDate;

    public movie(int ID, String name, String hebrewName, String director, String[] actors, String summary, Image image, String launchDate) {
        this.ID = ID;
        this.name = name;
        this.hebrewName = hebrewName;
        this.director = director;
        this.actors = actors;
        this.summary = summary;
        this.image = image;
        this.launchDate = launchDate;
    }

//    public boolean isNew(){
//
//    }
}
