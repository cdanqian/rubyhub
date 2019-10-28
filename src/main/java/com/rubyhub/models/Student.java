package com.rubyhub.models;

public class Student {
    String id = null;
    String firstName = null;
    String lastName = null;
    String bankAccount = null;

    public Student(String id, String firstName, String lastName, String bankAccount){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bankAccount = bankAccount;
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

}
