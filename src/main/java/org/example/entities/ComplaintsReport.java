package org.example.entities;

import org.example.entities.Report;

import java.time.LocalDate;

public class ComplaintsReport extends Report {
    public ComplaintsReport(int ID, LocalDate date) {
        super(ID,date);
    }

    public ComplaintsReport(){
        super();
    }


}

