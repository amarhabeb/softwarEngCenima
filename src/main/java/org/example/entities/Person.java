package org.example.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name ="person")

public abstract class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;
    protected String name;
    protected String phoneNum;
    protected String email;
    public static int id_counter=11;

        public Person(String name, String phoneNum, String email) {
            this.name = name;
            this.phoneNum = phoneNum;
            this.email = email;
            id_counter++;

        }


        public Person() {
        }

        public int getId_counter(){
            return id_counter;
        }

        public  int getID() {
            return id;
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
        
        @Override
        public String toString() {
        	return name;
        }

		public void setID(int iD) {
			id = iD;
		}
    }
