package org.example;

public abstract class person {
    protected int ID;
    protected String name;
    protected String phoneNum;
    protected String email;

        public person(int ID, String name, String phoneNum, String email) {
            this.ID = ID;
            this.name = name;
            this.phoneNum = phoneNum;
            this.email = email;
        }

        public person() {
            this.ID = 0;

        }

        public int getID() {
            return ID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
