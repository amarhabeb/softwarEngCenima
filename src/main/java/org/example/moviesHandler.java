package org.example;

import java.util.LinkedList;

public class moviesHandler {
    cinema Cinema;

    public moviesHandler(cinema Cinema) {
        this.Cinema = Cinema;
    }

    public LinkedList<show> loadShows(){
        return Cinema.getShows();
    }

   public LinkedList<movie> loadMovies(){
        return Cinema.getMovies();
   }


}
