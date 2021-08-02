package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="employee")

public class Employee extends Person {
    protected   String userName;
    protected   String password;
    protected boolean isOnline;
    protected int role;  // ContentManager: -1 , CustomerService: 0, ChainManager: 1 , CinemaManager: 2
    protected boolean active;

    public Employee(String name, String phoneNum, String email, String userName, String password, int role){
        super(name, phoneNum, email);
        this.userName = userName;
        this.password = password;
        this.isOnline=false;
        this.role=role;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
