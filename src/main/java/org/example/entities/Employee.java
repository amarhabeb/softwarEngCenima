package org.example.entities;

public class Employee extends Person {
    protected   String userName;
    protected   String password;

    public Employee(int ID, String name, String phoneNum, String email, String userName, String password){
        super(ID, name, phoneNum, email);
        this.userName = userName;
        this.password = password;

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
