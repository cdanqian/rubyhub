package com.rubyhub.http.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.client.MongoCollection;
import com.rubyhub.http.exceptions.HttpBadRequestException;
import com.rubyhub.http.responses.AppResponse;
import com.rubyhub.http.utils.PATCH;
import com.rubyhub.managers.StudentManager;
import com.rubyhub.models.Student;
import com.rubyhub.utils.*;
import org.bson.Document;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;


@Path("/students")
public class StudentHttpInterface extends HttpInterface{

    private ObjectWriter ow;
    private MongoCollection<Document> studentCollection = null;

    public StudentHttpInterface() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse postStudents(Object request){

        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            Student newStudent = new Student(
                    json.getString("id"),
                    json.getString("firstName"),
                    json.getString("lastName"),
                    json.getString("bankAccount"),
                    json.getString("email"),
                    json.getString("password")
            );
            StudentManager.getInstance().createStudent(newStudent);
            return new AppResponse("Insert Successful");

        }catch (Exception e){
            throw handleException("POST students", e);
        }

    }



    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getStudents(@Context HttpHeaders headers, @QueryParam("filter") String filter,@QueryParam("sortby") String sortby, @QueryParam("offset") Integer offset,
                                   @QueryParam("count") Integer count){
        try{
            AppLogger.info("Got an API call");
            ArrayList<Student> students = null;

            if(sortby != null)
                students = StudentManager.getInstance().getStudentListSorted(sortby);
            else if(filter != null)
                students = StudentManager.getInstance().getAllStudentsFiltered(filter);
            else if(offset != null && count != null)
                students = StudentManager.getInstance().getStudentListPaginated(offset, count);
            else
                students = StudentManager.getInstance().getStudentList();

            if(students != null)
                return new AppResponse(students);
            else
                throw new HttpBadRequestException(0, "Problem with getting students");
        }catch (Exception e){
            throw handleException("GET /students", e);
        }
    }


    @GET
    @Path("/{studentId}")
    @Produces({MediaType.APPLICATION_JSON})
    public AppResponse getSingleStudent(@Context HttpHeaders headers, @PathParam("studentId") String studentId){

        try{
            AppLogger.info("Got an API call");
            Student student = StudentManager.getInstance().getStudentById(studentId);

            if(student != null)
                return new AppResponse(student);
            else
                throw new HttpBadRequestException(0, "Problem with getting student");
        }catch (Exception e){
            throw handleException("GET /students/{studentId}", e);
        }


    }



    @PATCH
    @Path("/{studentId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public AppResponse patchStudent(@Context HttpHeaders headers, Object request, @PathParam("studentId") String studentId){

        JSONObject json = null;

        try{
            json = new JSONObject(ow.writeValueAsString(request));
            Student student = new Student(
                    studentId,
                    json.getString("firstName"),
                    json.getString("lastName"),
                    json.getString("bankAccount"),
                    json.getString("email"),
                    json.getString("password")
            );

            StudentManager.getInstance().updateStudent(headers, student);

        }catch (Exception e){
            throw handleException("PATCH students/{studentId}", e);
        }

        return new AppResponse("Update Successful");
    }


    @DELETE
    @Path("/{stuId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public AppResponse deleteStudents(@PathParam("stuId") String stuId){

        try{
            StudentManager.getInstance().deleteStudent( stuId);
            return new AppResponse("Delete Successful");
        }catch (Exception e){
            throw handleException("DELETE students/{stuId}", e);
        }

    }


}
