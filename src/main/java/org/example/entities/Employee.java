package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="employee")

public class Employee extends Person {
    protected   String userName;
    protected   String password;
    protected boolean active;

    public Employee(String name, String phoneNum, String email, String userName, String password){
        super(name, phoneNum, email);
        this.userName = userName;
        this.password = password;
        this.active=true;

    }
    public Employee(){
        super();
    }

      public String getUserName() {
          return userName;
      }

      public void setUserName(String userName) {
          this.userName = userName;
      }

      public String getPassword() {
          return password;
      }

      public void setPassword(String password) {
          this.password = password;
      }
  }
