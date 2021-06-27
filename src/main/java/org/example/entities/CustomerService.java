package org.example.entities;

public class CustomerService extends Employee{
    public CustomerService(String name, String phoneNum, String email, String userName, String password) {
        super(name, phoneNum, email, userName, password);
    }
    public CustomerService(){
        super();
    }
}
