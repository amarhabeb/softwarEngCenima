package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="manager")

public class Manager extends Employee {
    public Manager(String name, String phoneNum, String email, String userName, String password, int role) {
        super(name, phoneNum, email, userName, password, role);
    }
    public Manager(){
        super();
    }
}
