package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="customerservice")

public class CustomerService extends Employee{
    public CustomerService(String name, String phoneNum, String email, String userName, String password) {
        super(name, phoneNum, email, userName, password);
    }
    public CustomerService(){
        super();
    }
}
