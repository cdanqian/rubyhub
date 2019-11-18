package com.rubyhub.models;
import com.rubyhub.utils.APPCrypt;

import java.util.UUID;

public class Session {

    public String studentId = null;
    public String studentFirstName = null;
    public String studentLastName = null;
    public String studentBankAccount = null;
    public String token = null;

    public Session(Student student) throws Exception{
        this.studentId = student.id;
        //this.token = APPCrypt.encrypt(user.id);
        this.token = UUID.randomUUID().toString();
        this.studentFirstName = student.firstName;
        this.studentLastName = student.lastName;
        this.studentBankAccount = student.bankAccount;
    }

    public String getToken() {
        return token;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentFirstName(){
        return studentFirstName;
    }

    public String getStudentLastName(){
        return studentLastName;
    }
    public String getStudentBankAccount() {
        return studentBankAccount;
    }
}
