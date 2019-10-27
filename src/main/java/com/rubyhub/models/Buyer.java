package com.rubyhub.models;

import org.bson.Document;
import org.bson.types.Binary;

public class Buyer {
    private String id,firstname,lastname,phone;
    private Binary address;


    public Buyer(Document doc){
        this.id = doc.get("id").toString();
        this.firstname = doc.get("name").toString();
        this.lastname = doc.get("name").toString();
        this.phone = doc.get("phone").toString();

    }
    public Buyer(String id, String name, String phone){
        this.id = id;
        this.firstname = name;
        this.phone = phone;
    }

    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.firstname;
    }
    public String getPhone(){return this.phone;}
}
