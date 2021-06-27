package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="manger")

public class Manager extends Employee {
    public Manager(String name, String phoneNum, String email, String userName, String password) {
        super(name, phoneNum, email, userName, password);
    }
    public Manager(){
        super();
    }
}
