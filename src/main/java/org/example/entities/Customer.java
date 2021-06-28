package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="customer")
public class Customer extends Person {
    public Customer(String name, String phoneNum, String email){
        super(name, phoneNum, email);
    }
    public Customer(){
        super();
    }


}
