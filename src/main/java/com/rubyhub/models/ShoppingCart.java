package com.rubyhub.models;

import java.util.ArrayList;

public class ShoppingCart {
    public String id;
    public ArrayList<String> artworks;

    public ShoppingCart(String id, ArrayList<String> artworks){
        this.id = id;
        this.artworks = artworks;
    }

    public ArrayList<String> getArtworks(){
        return artworks;
    }

    public String getId(){
        return id;
    }
}
