package org.example.entities;

import org.example.entities.Report;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="complaintsreport")

public class ComplaintsReport extends Report {
    private int complaint_id;
    public ComplaintsReport( LocalDate date, int complaint_id) {
        super(date);
        this.complaint_id=complaint_id;
    }

    public ComplaintsReport(){
        super();
    }

    public int getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(int complaint_id) {
        this.complaint_id = complaint_id;
    }
}

