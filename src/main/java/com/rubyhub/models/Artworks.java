package com.rubyhub.models;

import com.rubyhub.managers.ArtworkManager;
import com.rubyhub.managers.BuyerManager;
import org.bson.Document;
import org.bson.types.Binary;

import java.util.Date;

public class Artworks {
    private String id,name, description;
    private Double price;
    private Boolean deleted;
    private Date createdOn, updatedOn, deletedOn;


    public Artworks(Document doc) {
        this.id = doc.get(ArtworkManager.FIELD_ID).toString();
        this.name = doc.getString(ArtworkManager.FIELD_NAME);
        this.description = doc.getString(ArtworkManager.FIELD_DESC);
        this.deleted = doc.getBoolean(BuyerManager.FIELD_DELETED);
        this.createdOn = doc.getDate(BuyerManager.FIELD_CREATED_ON);
        this.updatedOn = doc.getDate(BuyerManager.FIELD_UPDATED_ON);
        try {
            this.deletedOn = doc.getDate(BuyerManager.FIELD_DELETED_ON);
        } catch (Exception e) {
        }
        
    }
}
