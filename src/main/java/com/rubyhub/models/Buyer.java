package com.rubyhub.models;

import org.bson.Document;
import org.bson.types.Binary;

public class Buyer {
    private String id,firstname,lastname,;
    private Binary address;
    private String phone;


    public Buyer(Document doc){
        this.id = doc.get("id").toString();
        this.name = doc.get("name").toString();
        this.phone = doc.get("phone").toString();

    }
    public Buyer(String id, String name, String phone){
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getPhone(){return this.phone;}
}
