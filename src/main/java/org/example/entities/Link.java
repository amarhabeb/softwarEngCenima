package org.example.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="link")

public class Link extends Order{
    private String link;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;
    private int movie_id;




    public Link(String link, LocalDateTime fromTime, LocalDateTime toTime , int movie_id, double price, Payment payment) {
        super(price, payment);
        this.link = link;
        this.fromTime=fromTime;
        this.toTime=toTime;
        this.movie_id=movie_id;
    }

    public Link(){
        super();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalDateTime toTime) {
        this.toTime = toTime;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }
}
