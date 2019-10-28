package com.rubyhub.models;

import com.rubyhub.managers.BuyerManager;
import org.bson.Document;
import org.bson.types.Binary;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.Date;

public class Buyer {
    private String id, firstname, lastname, phone, email, password, city, country, paymentMethod, paymentAccount;
    private Binary address;
    private Boolean deleted;
    private Date createdOn, updatedOn, deletedOn;


    public Buyer(Document doc) {
        this.id = doc.get(BuyerManager.FIELD_ID).toString();
        this.firstname = doc.get(BuyerManager.FIELD_FNAME).toString();
        this.lastname = doc.get(BuyerManager.FIELD_LNAME).toString();
        this.email = doc.get(BuyerManager.FIELD_EMAIL).toString();
        this.password = doc.get(BuyerManager.FIELD_PW).toString();

        this.phone = doc.getString(BuyerManager.FIELD_PHONE); // .toString() fails with null;

        this.city = doc.getString(BuyerManager.FIELD_CITY);
        this.country = doc.getString(BuyerManager.FIELD_COUNTRY);
        this.paymentMethod = doc.getString(BuyerManager.FIELD_PAYMENT_METHOD);
        this.paymentAccount = doc.getString(BuyerManager.FIELD_PAYMENT_ACCOUNT);
        this.deleted = doc.getBoolean(BuyerManager.FIELD_DELETED);
        try {
            this.address = doc.get(BuyerManager.FIELD_ADDRESS, org.bson.types.Binary.class);
        } catch (Exception e) {
        }

        try {
            this.createdOn = doc.getDate(BuyerManager.FIELD_CREATED_ON);
        } catch (Exception e) {
        }
        try {
            this.updatedOn = doc.getDate(BuyerManager.FIELD_UPDATED_ON);
        } catch (Exception e) {
        }
        try {
            this.deletedOn = doc.getDate(BuyerManager.FIELD_DELETED_ON);
        } catch (Exception e) {
        }
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.firstname;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return this.country;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public String getPaymentAccount() {
        return this.paymentAccount;
    }


    public JSONArray getAddress() {
        try {
            return new JSONArray(new String(address.getData()));
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public Date getDeletedOn() {
        return deletedOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public JSONObject castToJSON() {
        try {
            JSONObject js = new JSONObject().put(BuyerManager.FIELD_ID, getId())
                    .put(BuyerManager.FIELD_FNAME, getFirstname())
                    .put(BuyerManager.FIELD_LNAME, getLastname())
                    .put(BuyerManager.FIELD_PHONE, getPhone())
                    .put(BuyerManager.FIELD_EMAIL, getEmail())
                    .put(BuyerManager.FIELD_PW, getPassword())
                    .put(BuyerManager.FIELD_CITY, getCity())
                    .put(BuyerManager.FIELD_COUNTRY, getCountry())
                    .put(BuyerManager.FIELD_PAYMENT_METHOD, getPaymentMethod())
                    .put(BuyerManager.FIELD_PAYMENT_ACCOUNT, getPaymentAccount())
                    .put(BuyerManager.FIELD_ADDRESS, getAddress())
                    .put(BuyerManager.FIELD_DELETED, getDeleted())
                    .put(BuyerManager.FIELD_CREATED_ON, getCreatedOn())
                    .put(BuyerManager.FIELD_UPDATED_ON, getUpdatedOn())
                    .put(BuyerManager.FIELD_DELETED_ON, getDeletedOn());
            return js;
        } catch (Exception e) {
            return null;
        }
    }
}
