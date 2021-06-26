package org.example.entities;

import org.example.entities.Report;

import java.time.LocalDate;

public class RefundsReport extends Report {
    public RefundsReport(LocalDate date){
        this.date = date;
    }
    public RefundsReport(){super();}


}
