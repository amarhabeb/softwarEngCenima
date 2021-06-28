package org.example.entities;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="LinksPackagesReport")

public class LinksPackagesReport extends Report{
    public LinksPackagesReport(LocalDate date){
        this.date = date;
    }
    public LinksPackagesReport(){super();}

}
