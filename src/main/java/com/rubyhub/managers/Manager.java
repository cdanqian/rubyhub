package com.rubyhub.managers;

import com.mongodb.client.MongoCollection;
import com.rubyhub.exceptions.AppException;
import com.rubyhub.exceptions.AppInternalServerException;
import com.rubyhub.utils.AppLogger;
import com.rubyhub.utils.MongoPool;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Manager {
    public static String FIELD_DELETED = "deleted", FIELD_CREATED_ON = "createdOn", FIELD_UPDATED_ON = "updatedOn", FIELD_DELETED_ON = "deletedOn";
    protected static Bson FILTER_NOT_DELETED = eq(FIELD_DELETED, false);
    protected MongoCollection<Document> studentCollection, buyerCollection, artworkCollection, artworkImageCollection;

    public Manager() {
        this.studentCollection = MongoPool.getInstance().getCollection(MongoPool.studentCollectionName);
        this.buyerCollection = MongoPool.getInstance().getCollection(MongoPool.buyerCollectionName);
        this.artworkCollection = MongoPool.getInstance().getCollection(MongoPool.artworkCollectionName);
        this.artworkImageCollection = MongoPool.getInstance().getCollection(MongoPool.artworkImageCollectionName);
    }

    protected AppException handleException(String message, Exception e) {
        AppLogger.error(message, e);
        if ((e instanceof AppException) && !(e instanceof AppInternalServerException)) {
            return (AppException) e;
        }
        return (AppInternalServerException) e;
    }

    public static List<String> convertJSONArrayToList(JSONArray input){
        List<String> list = new ArrayList<String>();
        for (int i=0; i<input.length(); i++) {
            try {
                list.add( input.getString(i) );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
