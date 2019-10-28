package com.rubyhub.managers;

import com.mongodb.client.MongoCollection;
import com.rubyhub.exceptions.AppException;
import com.rubyhub.exceptions.AppInternalServerException;
import com.rubyhub.utils.AppLogger;
import com.rubyhub.utils.MongoPool;
import org.bson.Document;

import javax.ws.rs.PUT;

public class Manager {
    protected MongoCollection<Document> studentCollection, buyerCollection;

    public Manager(){
        this.studentCollection = MongoPool.getInstance().getCollection(MongoPool.studentCollectionName);
        this.buyerCollection = MongoPool.getInstance().getCollection(MongoPool.buyerCollectionName);
    }
    protected AppException handleException(String message, Exception e){
        AppLogger.error(message, e);
        if((e instanceof AppException) && !(e instanceof AppInternalServerException)){
            return (AppException) e;
        }
        return (AppInternalServerException) e;
    }
}
