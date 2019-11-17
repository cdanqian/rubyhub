package com.rubyhub.models;

import com.rubyhub.managers.ArtworkManager;
import com.rubyhub.managers.BuyerManager;
import org.bson.Document;
import org.bson.types.Binary;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.Date;
import java.util.List;

public class Artwork {
    private String id, name, description, student;
    private Double price;
    private List<String> sizes, styles;
    private Boolean passedcheck, deleted;
    private Date createdOn, updatedOn, deletedOn;
    private String image;

    public Artwork(Document doc) {
        this.id = doc.get(ArtworkManager.FIELD_ID).toString();
        this.name = doc.getString(ArtworkManager.FIELD_NAME);
        this.description = doc.getString(ArtworkManager.FIELD_DESC);
        this.price = doc.getDouble(ArtworkManager.FIELD_PRICE);
        this.sizes = doc.get(ArtworkManager.FIELD_SIZE, List.class);
        this.styles = doc.get(ArtworkManager.FIELD_STYLES, List.class);
        this.student = doc.getString(ArtworkManager.FIELD_STUDENT);
        this.passedcheck = doc.getBoolean(ArtworkManager.FIELD_PASS_CHECK);
        this.deleted = doc.getBoolean(BuyerManager.FIELD_DELETED);
        this.createdOn = doc.getDate(BuyerManager.FIELD_CREATED_ON);
        this.updatedOn = doc.getDate(BuyerManager.FIELD_UPDATED_ON);
        try {
            this.deletedOn = doc.getDate(BuyerManager.FIELD_DELETED_ON);
        } catch (Exception e) {
        }
    }

    public void setImage(Binary image) {
        this.image = new String(image.getData());
    }

    public JSONObject castToJSON() {
        try {
            JSONObject js = new JSONObject().put("id", id)
                    .put(ArtworkManager.FIELD_NAME, name)
                    .put(ArtworkManager.FIELD_DESC, description)
                    .put(ArtworkManager.FIELD_PRICE, price)
                    .put(ArtworkManager.FIELD_SIZE, new JSONArray(sizes))
                    .put(ArtworkManager.FIELD_STYLES, new JSONArray(styles))
                    .put(ArtworkManager.FIELD_STUDENT, student)
                    .put(ArtworkManager.FIELD_PASS_CHECK, passedcheck)
                    .put(ArtworkManager.FIELD_IMAGE_CONTENT, "")
                    .put(BuyerManager.FIELD_DELETED, deleted)
                    .put(BuyerManager.FIELD_CREATED_ON, createdOn)
                    .put(BuyerManager.FIELD_UPDATED_ON, updatedOn)
                    .put(BuyerManager.FIELD_DELETED_ON, deletedOn);
            return js;
        } catch (Exception e) {
            return null;
        }
    }
}
