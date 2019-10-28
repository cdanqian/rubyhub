package com.rubyhub.managers;

import com.rubyhub.models.Buyer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.codehaus.jettison.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

public class BuyerManager extends Manager {
    public static BuyerManager _self;
    public static String FIELD_ID = "_id",
            FIELD_FNAME = "firstname", FIELD_LNAME = "lastname", FIELD_PHONE = "phone", FIELD_CITY = "city", FIELD_COUNTRY = "country",
            FIELD_EMAIL = "email", FIELD_PW = "passwrod", FIELD_PAYMENT_METHOD = "paymentMethod", FIELD_PAYMENT_ACCOUNT = "paymentAccount",
            FIELD_ADDRESS = "address", FIELD_DELETED = "deleted", FIELD_CREATED_ON = "createdOn", FIELD_UPDATED_ON = "updatedOn", FIELD_DELETED_ON = "deletedOn";

    public static BuyerManager getInstance() {
        if (_self == null) {
            return new BuyerManager();
        }
        return _self;
    }

    public void create(String fname, String lname, String email, String pw) {
        this.buyerCollection.insertOne(new Document(FIELD_FNAME, fname)
                .append(FIELD_LNAME, lname)
                .append(FIELD_EMAIL, email)
                .append(FIELD_PW, pw)
                .append(FIELD_PHONE, "")
                .append(FIELD_CITY, "")
                .append(FIELD_COUNTRY, "")
                .append(FIELD_ADDRESS, "")
                .append(FIELD_PAYMENT_ACCOUNT, "")
                .append(FIELD_PAYMENT_METHOD, "")
                .append(FIELD_DELETED, false)
                .append(FIELD_CREATED_ON, new Date())
                .append(FIELD_UPDATED_ON, new Date())
                .append(FIELD_DELETED_ON, null)
        );
    }

    public void delete(String id) {
        Bson filter = eq(FIELD_ID, id);
        Bson content = new Document("$set", new Document(FIELD_DELETED, true).append(FIELD_DELETED_ON, new Date()));
//        this.buyerCollection.deleteOne(new Document(FIELD_ID, id));
    }

    public Buyer updateBasicInfo(String id, String fname, String lname, String phone, String city, String country, JSONArray address) {
        Bson filter = eq(FIELD_ID, id);
        Bson content = new Document("$set", new Document(FIELD_FNAME, fname)
                .append(FIELD_LNAME, lname)
                .append(FIELD_PHONE, phone)
                .append(FIELD_CITY, city)
                .append(FIELD_COUNTRY, country)
                .append(FIELD_ADDRESS, address.toString().getBytes())
                .append(FIELD_UPDATED_ON, new Date()));
        this.buyerCollection.findOneAndUpdate(filter, content);
        return getById(id);
    }

    public Buyer updatePaymentInfo(String id, String pmethod, String paccount) {
        Bson filter = eq(FIELD_ID, id);
        Bson content = new Document("$set", new Document(FIELD_PAYMENT_ACCOUNT, paccount)
                .append(FIELD_PAYMENT_METHOD, pmethod)
                .append(FIELD_UPDATED_ON, new Date()));
        Document doc = this.buyerCollection.findOneAndUpdate(filter, content);
        return getById(id);
    }

    public Buyer updatePW(String id, String pw) {
        Bson filter = eq(FIELD_ID, id);
        Bson content = new Document("$set", new Document(FIELD_PW, pw)).append(FIELD_UPDATED_ON, new Date());
        Document doc = this.buyerCollection.findOneAndUpdate(filter, content);
        return getById(id);
    }

    public Buyer getById(String id) {
        Document doc = this.buyerCollection.find(new Document(FIELD_ID, id)).first();
        if (doc != null)
            return new Buyer(doc);
        else
            return null;
    }

    public Buyer getByEmail(String email) {
        Document doc = this.buyerCollection.find(new Document(FIELD_EMAIL, email)).first();
        if (doc != null)
            return new Buyer(doc);
        else
            return null;
    }

    public List<Buyer> getAllBuyers() {
        List<Buyer> sList = new ArrayList<Buyer>();
        Iterable<Document> docs = this.buyerCollection.find(new Document(FIELD_DELETED, false));
        if (docs != null) {
            docs.forEach(doc -> sList.add(new Buyer(doc)));
            return sList;
        } else {
            return null;
        }
    }

    public List<Buyer> getAllBuyersSorted(String sortby) {
        List<Buyer> sList = new ArrayList<Buyer>();
        Iterable<Document> docs = this.buyerCollection.find(new Document(FIELD_DELETED, false));
        if (docs != null) {
            docs.forEach(doc -> sList.add(new Buyer(doc)));
            return sList;
        } else {
            return null;
        }
    }

    public List<Buyer> getAllBuyersFiltered(String filterBy) {
        List<Buyer> sList = new ArrayList<Buyer>();
        Bson filter = null;

        switch (filterBy) {
            case "newUsers":
                filter = gt(FIELD_CREATED_ON, new Date(System.currentTimeMillis() - (7 * 1000 * 60 * 60 * 24)));
        }
        Iterable<Document> docs = this.buyerCollection.find(filter);
        if (docs != null) {
            docs.forEach(doc -> sList.add(new Buyer(doc)));
            return sList;
        } else {
            return null;
        }
    }

    public List<Buyer> getAllBuyersPaginated(Integer offset, Integer count) {
        Document sortParams = new Document();
        sortParams.put("riderBalance", 1);
        List<Buyer> sList = new ArrayList<Buyer>();
        Iterable<Document> docs = this.buyerCollection.find(new Document(FIELD_DELETED, false)).sort(sortParams).skip(offset).limit(count);
        if (docs != null) {
            docs.forEach(doc -> sList.add(new Buyer(doc)));
            return sList;
        } else {
            return null;
        }
    }
}
