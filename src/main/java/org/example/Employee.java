package org.example;

  public class Employee extends Person{
    protected   String userName;
    protected   String password;

    public Employee(int ID, String name, String phoneNum, String email, String userName, String password){
        this.ID = ID;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.userName = userName;
        this.password = password;

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
