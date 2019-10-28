package com.rubyhub.managers;

import com.rubyhub.models.Buyer;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class BuyerManager extends Manager {
    public static BuyerManager _self;
    public static String FIELD_ID = "_id",
            FIELD_FNAME = "firstname", FIELD_LNAME = "lastname", FIELD_PHONE = "phone", FIELD_CITY = "city", FIELD_COUNTRY = "country",
            FIELD_EMAIL = "email", FIELD_PW = "passwrod", FIELD_PAYMENT_METHOD = "paymentMethod", FIELD_PAYMENT_ACCOUNT = "paymentAccount",
            FIELD_ADDRESS = "address", FIELD_DELETED = "deleted", FIELD_CREATED_ON = "createdOn", FIELD_UPDATED_ON = "updatedOn", FIELD_DELETED_ON = "deletedOn";
    private static Bson FILTER_NOT_DELETED = eq(FIELD_DELETED, false);

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
                .append(FIELD_CITY, "de")
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
        Bson filter = eq(FIELD_ID, new ObjectId(id));
        Bson content = new Document("$set", new Document(FIELD_DELETED, true).append(FIELD_DELETED_ON, new Date()));
        this.buyerCollection.findOneAndUpdate(filter, content);
    }

    public Buyer updateBasicInfo(String id, String fname, String lname, String phone, String city, String country, JSONObject address) {
        Bson filter = eq(FIELD_ID, new ObjectId(id));
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
        Bson filter = eq(FIELD_ID, new ObjectId(id));
        Bson content = new Document("$set", new Document(FIELD_PAYMENT_ACCOUNT, paccount)
                .append(FIELD_PAYMENT_METHOD, pmethod)
                .append(FIELD_UPDATED_ON, new Date()));
        Document doc = this.buyerCollection.findOneAndUpdate(filter, content);
        return getById(id);
    }

    public Buyer updatePW(String id, String pw) {
        Bson filter = eq(FIELD_ID, new ObjectId(id));
        Bson content = new Document("$set", new Document(FIELD_PW, pw)).append(FIELD_UPDATED_ON, new Date());
        Document doc = this.buyerCollection.findOneAndUpdate(filter, content);
        return getById(id);
    }

    public Buyer getById(String id) {
        try {
            Document doc = this.buyerCollection.find(and(FILTER_NOT_DELETED, new Document(FIELD_ID, new ObjectId(id)))).first();
            return new Buyer(doc);
        } catch (Exception err) {
            return null;
        }
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
        Iterable<Document> docs = this.buyerCollection.find(FILTER_NOT_DELETED);
        if (docs != null) {
            docs.forEach(doc -> sList.add(new Buyer(doc)));
            return sList;
        } else {
            return null;
        }
    }

    public List<Buyer> getAllBuyersSorted(String sortby) {
        List<Buyer> sList = new ArrayList<Buyer>();
        Document sort = null;
        switch (sortby) {
            case "nameAsc":
                sort = new Document(FIELD_FNAME, 1);
                break;
            case "nameDesc":
                sort = new Document(FIELD_FNAME, -1);
                break;
        }
        Iterable<Document> docs = this.buyerCollection.find(FILTER_NOT_DELETED).sort(sort);
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
                break;
            case "luckyUsers":
                filter = regex(FIELD_FNAME, "^d", "i"); // lucky users: first name start with letter D
                break;

        }
        Iterable<Document> docs = this.buyerCollection.find(and(FILTER_NOT_DELETED, filter));
        if (docs != null) {
            docs.forEach(doc -> sList.add(new Buyer(doc)));
            return sList;
        } else {
            return null;
        }
    }

    public List<Buyer> getAllBuyersPaginated(Integer offset, Integer count) {
        List<Buyer> sList = new ArrayList<Buyer>();
        Iterable<Document> docs = this.buyerCollection.find(FILTER_NOT_DELETED).sort(eq(FIELD_FNAME, 1)).skip(offset).limit(count);
        if (docs != null) {
            docs.forEach(doc -> sList.add(new Buyer(doc)));
            return sList;
        } else {
            return null;
        }
    }

    public void create(String fname, String lname, String email, String pw, String phone, String city, String country, Document address, String pm, String pa) {
        this.buyerCollection.insertOne(new Document(FIELD_FNAME, fname)
                .append(FIELD_LNAME, lname)
                .append(FIELD_EMAIL, email)
                .append(FIELD_PW, pw)
                .append(FIELD_PHONE, phone)
                .append(FIELD_CITY, city)
                .append(FIELD_COUNTRY, country)
                .append(FIELD_ADDRESS, address.toString().getBytes())
                .append(FIELD_PAYMENT_ACCOUNT, pa)
                .append(FIELD_PAYMENT_METHOD, pm)
                .append(FIELD_DELETED, false)
                .append(FIELD_CREATED_ON, new Date())
                .append(FIELD_UPDATED_ON, new Date())
                .append(FIELD_DELETED_ON, null)
        );
    }

    public void resetDB() {
        try {
                create("cdan", "aa", "abc@app.com", "123", "6502011111", "Fremont", "US", new Document().append("add1", "add1").append("add2", "add2").append("zipcode", "94536") , "credit", "2109990000");
                create("ddan", "bb", "cbc@app.com", "234", "6502011112", "Fremont", "US", new Document().append("add1", "add1").append("add2", "add2").append("zipcode", "94536") , "credit", "2109990001");
                create("qddn", "cc", "vbc@app.com", "123", "6502011113", "San Jose", "US", new Document().append("add1", "add1").append("add2", "add2").append("zipcode", "94536"), "credit", "2109990002");
                create("wdan", "dd", "bbc@app.com", "123", "6502011114", "San Jose", "US", new Document().append("add1", "add1").append("add2", "add2").append("zipcode", "94536"), "credit", "2109990003");
                create("edan", "ee", "nbc@app.com", "123", "6502011115", "San Jose", "US", new Document().append("add1", "add1").append("add2", "add2").append("zipcode", "94536"), "credit", "2109990004");
                create("rdan", "rr", "mbc@app.com", "123", "6502011116", "San Jose", "US", new Document().append("add1", "add1").append("add2", "add2").append("zipcode", "94536"), "credit", "2109990005");
                create("ddan", "tt", "qbc@app.com", "123", "6502011117", "San Jose", "US", new Document().append("add1", "add1").append("add2", "add2").append("zipcode", "94536"), "credit", "2109990006");
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
        }
    }

}
