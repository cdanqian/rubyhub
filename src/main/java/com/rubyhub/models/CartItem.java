package com.rubyhub.models;

public class CartItem {
    public String id, name;
    public Double price;
    public String image;
    public String size;
    public CartItem(String id, String name, Double price, String image, String size){
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.size = size;
    }

    public String getSize(){
        return size;
    }
}
