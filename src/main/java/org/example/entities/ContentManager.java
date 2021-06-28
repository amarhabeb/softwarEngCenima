package org.example.entities;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="contentmanger")
public class ContentManager extends Manager{
    public ContentManager(String name, String phoneNum, String email, String userName, String password) {
        super(name, phoneNum, email, userName, password);
    }
    public ContentManager(){
        super();
    }
}
