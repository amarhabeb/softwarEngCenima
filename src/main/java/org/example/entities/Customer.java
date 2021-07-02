package org.example.entities;
import org.example.Controllers.*;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.cfg.Configuration;

@Entity
@Table(name ="customer")
public class Customer extends Person {
	private List<Complaint> complaints;;

    public Customer(String name, String phoneNum, String email){
        super(name, phoneNum, email);
    }
    public Customer(){
        super();
    }
	/*public List<Complaint> getComplaint() throws Exception { /*??
		return ComplaintsController.loadComplaints(null);
	} */
    
    public List<Complaint> getComplaints() {
        return complaints;
    }

}
