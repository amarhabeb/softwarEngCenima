package org.example;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


import javax.persistence.*;
import java.util.*;
@Entity
@Table(name ="movie")

public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name_en;
    private String name_heb;
    private String director;
    private String summary;
    private String lanuch_date;
    private Boolean is_new;
    private String image;
    private String status; // can be Available / not available
    @OneToMany(targetEntity = show.class)
    private List<show> shows;

    public Movie() {
        super();
    }


    public Movie(String name_en, String name_heb, String director, String summary, String lanuch_date, Boolean is_new, String image, List<show> shows) {
        super();
        this.name_en = name_en;
        this.name_heb = name_heb;
        this.director = director;
        this.summary = summary;
        this.lanuch_date = lanuch_date;
        this.is_new = is_new;
        this.image = image;
        this.shows = shows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIs_new() {
        return is_new;
    }

    public List<show> getShows() {
        return shows;
    }

    public void setShows(List<show> shows) {
        this.shows = shows;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_heb() {
        return name_heb;
    }

    public void setName_heb(String name_heb) {
        this.name_heb = name_heb;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLanuch_date() {
        return lanuch_date;
    }

    public void setLanuch_date(String lanuch_date) {
        this.lanuch_date = lanuch_date;
    }


    public void setIs_new(Boolean is_new) {
        this.is_new = is_new;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
    }

}






