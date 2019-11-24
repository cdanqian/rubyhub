package com.rubyhub.models;

public class Student {
    String id = null;
    String firstName = null;
    String lastName = null;
    String bankAccount = null;
    String email = null;
    String password = null;

    public Student(String id, String firstName, String lastName, String bankAccount, String email, String password){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankAccount = bankAccount;
        this.email = email;
        this.password = password;
    }
    public String getId(){
        return id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getBankAccount(){
        return bankAccount;
    }
    public String getEmail(){ return email; }
    public String getPassword(){ return password; }

    public void setId(String id) {
        this.id = id;
    }
}