package org.example.entities;

import org.example.entities.Manager;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="chainmanager")

public class ChainManager extends Manager {
    public ChainManager(String name, String phoneNum, String email, String userName, String password) {
        super(name, phoneNum, email, userName, password,1);
    }

    public ChainManager(){
        super();
    }

}
