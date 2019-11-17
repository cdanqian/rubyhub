package com.rubyhub.utils;

import com.mongodb.client.*;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoPool {
    public static String buyerCollectionName = "buyers",
                        studentCollectionName = "students",
                        artworkCollectionName="artworks",artworkImageCollectionName="artworks.images";
    private static MongoPool mp;
    private static MongoDatabase db;

    // Singleton for creating one MongoDB client
    private MongoPool(){
        try {
//            MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
//            //build the connection options
//            builder.maxConnectionIdleTime(60000);//set the max wait time in (ms)
//            builder.socketKeepAlive(true);
//            builder.connectTimeout(30000);
//            MongoClientOptions opts = builder.build();

//            MongoClient mc = new MongoClient(new ServerAddress(Config.dbHost, Config.dbPort));
            MongoClient mc = MongoClients.create();
            db = mc.getDatabase(Config.database);
            Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        } catch (Exception e) {
            AppLogger.error("From MongoPool creation ",e);
        }

    }

    public static MongoPool getInstance(){
        if (mp == null ){
            mp = new MongoPool();
        }
        return mp;
    }

    public MongoCollection<Document> getCollection(String collectionName){
        MongoCollection<Document> collection;
        return db.getCollection(collectionName);
    }
    public void resetDB(){
        db.drop();
    }
}
