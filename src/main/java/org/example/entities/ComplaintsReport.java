package org.example.entities;

import org.example.entities.Report;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="complaintsreport")

public class ComplaintsReport extends Report {
    public ComplaintsReport( LocalDate date) {
        super(date);
    }

    public ComplaintsReport(){
        super();
    }


}

