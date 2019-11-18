package com.rubyhub.managers;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.rubyhub.exceptions.AppException;
import com.rubyhub.exceptions.AppInternalServerException;
import com.rubyhub.exceptions.AppUnauthorizedException;
import com.rubyhub.models.Session;
import com.rubyhub.models.Student;
import com.rubyhub.utils.MongoPool;
import com.rubyhub.utils.AppLogger;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import javax.ws.rs.core.HttpHeaders;
import java.lang.String;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.*;

public class StudentManager extends Manager{
    public static StudentManager _self;
    public MongoCollection<Document> studentCollection;

    public StudentManager() {
        this.studentCollection = MongoPool.getInstance().getCollection("students");
        if (studentCollection.count() == 0) {
            collectionInsertStudent("1", "Junruo", "Tian", "2342938", "junruo.tian@sv.cmu.edu", "1234567");
            collectionInsertStudent("2", "Hongbin", "Li", "948543", "hongbin.li@sv.cmu.edu", "1234567");
            collectionInsertStudent("3", "Dan", "Qian", "4894893", "qian.dan@sv.cmu.edu", "1234567");
            collectionInsertStudent("4", "Andy", "Truot","42049248", "Andy.Trout@sv.cmu.edu", "1234567");
            collectionInsertStudent("5", "Henry", "Liu","34492533", "Henry.Liu@sv.cmu.edu", "123456");
        }
    }

    public static StudentManager getInstance(){
        if (_self == null)
            _self = new StudentManager();
        return _self;
    }

    public void createStudent(Student student) throws AppException {

        try{
            JSONObject json = new JSONObject(student);

            Document newDoc = new Document()
                    .append("id", student.getId())
                    .append("firstName", student.getFirstName())
                    .append("lastName",student.getLastName())
                    .append("bankAccount",student.getBankAccount())
                    .append("email", student.getEmail())
                    .append("password", student.getPassword());
            if (newDoc != null)
                studentCollection.insertOne(newDoc);
            else
                throw new AppInternalServerException(0, "Failed to create new student");

        }catch(Exception e){
            throw handleException("Create Student", e);
        }

    }

    public void updateStudent(HttpHeaders headers, Student student) throws AppException {
        try {
            Session session = SessionManager.getInstance().getSessionForToken(headers);
            if(!session.getStudentId().equals(student.getId()))
                throw new AppUnauthorizedException(70,"Invalid student id");

            Bson filter = new Document("_id", new ObjectId(student.getId()));
            Bson newValue = new Document()
                    .append("firstName", student.getFirstName())
                    .append("lastName", student.getLastName())
                    .append("bankAccount", student.getBankAccount())
                    .append("email",student.getEmail()).append("password",student.getPassword());
            Bson updateOperationDocument = new Document("$set", newValue);

            if (newValue != null)
                studentCollection.updateOne(filter, updateOperationDocument);
            else
                throw new AppInternalServerException(0, "Failed to update student details");

        }
        catch(AppUnauthorizedException e) {
            throw new AppUnauthorizedException(34, e.getMessage());
        }
        catch(Exception e) {
            throw handleException("Update User", e);
        }
    }

    public void deleteStudent(String stuId) throws AppException {
        try {
            Bson filter = new Document("id", stuId);
            studentCollection.deleteOne(filter);
        }catch (Exception e){
            throw handleException("Delete Student", e);
        }
    }

    public ArrayList<Student> getStudentList() throws AppException {
        try{
            ArrayList<Student> studentList = new ArrayList<>();
            FindIterable<Document> studentDocs = studentCollection.find();
            for(Document stuDoc: studentDocs) {
                Student student = new Student(
                        stuDoc.getString("id"),
                        stuDoc.getString("firstName"),
                        stuDoc.getString("lastName"),
                        stuDoc.getString("bankAccount"),
                        stuDoc.getString("email"),
                        stuDoc.getString("password")
                );
                studentList.add(student);
            }
            return new ArrayList<>(studentList);
        } catch(Exception e){
            throw handleException("Get Borrower List", e);
        }
    }

    public Student getStudentById(String id) throws AppException {
        try{
            FindIterable<Document> stuDocs = studentCollection.find();
            for(Document stuDoc: stuDocs) {
                if(stuDoc.getString("id").equals(id)) {
                    Student student = new Student(
                            stuDoc.getString("id"),
                            stuDoc.getString("firstName"),
                            stuDoc.getString("lastName"),
                            stuDoc.getString("bankAccount"),
                            stuDoc.getString("email"),
                            stuDoc.getString("password")
                    );
                    return student;
                }
            }
            return null;
        } catch(Exception e){
            throw handleException("Get Student ", e);
        }
    }

    public ArrayList<Student> getStudentListPaginated(Integer offset, Integer count) throws AppException {
        try{
            ArrayList<Student> studentList = new ArrayList<>();
            FindIterable<Document> stuDocs = studentCollection.find().skip(offset).limit(count);
            for(Document stuDoc: stuDocs) {
                Student student = new Student(
                        stuDoc.getString("id"),
                        stuDoc.getString("firstName"),
                        stuDoc.getString("lastName"),
                        stuDoc.getString("bankAccount"),
                        stuDoc.getString("email"),
                        stuDoc.getString("password")
                );
                studentList.add(student);
            }
            return new ArrayList<>(studentList);
        } catch(Exception e){
            throw handleException("Get Student List", e);
        }
    }

    public ArrayList<Student> getAllStudentsFiltered(String filterBy) {
        ArrayList<Student> studentList = new ArrayList<>();
        Bson filter = null;
        filter = Filters.eq("lastName", filterBy);
        Iterable<Document> stuDocs = this.studentCollection.find(filter);
        if(stuDocs != null){
            for(Document stuDoc: stuDocs) {
                Student student = new Student(
                        stuDoc.getString("id"),
                        stuDoc.getString("firstName"),
                        stuDoc.getString("lastName"),
                        stuDoc.getString("bankAccount"),
                        stuDoc.getString("email"),
                        stuDoc.getString("password")
                );
                studentList.add(student);
            }
        }
        else {
            return null;
        }
        return new ArrayList<>(studentList);
    }

    public ArrayList<Student> getStudentListSorted(String sortby) throws AppException {
        try{
            ArrayList<Student> studentList = new ArrayList<>();
            BasicDBObject sortParams = new BasicDBObject();
            sortParams.put(sortby, -1);
            FindIterable<Document> stuDocs = studentCollection.find().sort(sortParams);
            for(Document stuDoc: stuDocs) {
                Student student = new Student(
                        stuDoc.getString("id"),
                        stuDoc.getString("firstName"),
                        stuDoc.getString("lastName"),
                        stuDoc.getString("bankAccount"),
                        stuDoc.getString("email"),
                        stuDoc.getString("password")
                );
                studentList.add(student);
            }
            return new ArrayList<>(studentList);
        } catch(Exception e){
            throw handleException("Get Student List", e);
        }
    }

    public void collectionInsertStudent(String id, String firstName, String lastName, String bankAccount, String email, String password) {
        Document document = new Document("id",id).append("firstName",firstName).append("lastName",lastName).append("bankAccount", bankAccount).append("email", email).append("password",password);
        studentCollection.insertOne(document);
    }
}
