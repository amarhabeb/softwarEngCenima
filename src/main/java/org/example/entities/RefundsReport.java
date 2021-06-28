package org.example.entities;

import org.example.entities.Report;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="refundsreport")

public class RefundsReport extends Report {

    public RefundsReport(LocalDate date){
        this.date = date;
    }
    public RefundsReport(){super();}


}
