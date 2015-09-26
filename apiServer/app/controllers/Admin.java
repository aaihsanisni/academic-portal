package controllers;

import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.util.*;

// import com.mongodb.client.AggregateIterable;
// import org.bson.Document;

import static java.util.Arrays.asList;

import play.*;
import play.data.Form;
import play.data.DynamicForm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import play.mvc.*;
import service.MongoQueryEngine;
import sun.io.ByteToCharDBCS_ASCII;
import views.html.*;
import akka.io.Tcp.Bind;
import play.data.validation.Constraints;

import com.mongodb.*;

/**
 * Created by atul on 14/09/15.
 */
public class Admin extends Controller{
    public static class AddForm {
        public String id;
        public String name;
        public String dept;
    };

    public static class CourseOfferingForm {
        public String course_id;
        public String faculty_id;
    };

    public static class RegisterStudentForm{
        public String entryno;
        public String course_id;
    };


    public static class RegisterCourseOffering{
        public String faculty_id;
        public String course_id;
        public String course_limit;
    };

    public static Result courseOfferingData() {
        BasicDBList returnList = new BasicDBList();
        DB db = Application.acadDB();
        DBCollection facultyCol = db.getCollection(Play.application().configuration()
                .getString("mongo.faculty"));
        DBCollection courseOfferingCol = db.getCollection(Play.application().configuration().
                getString("mongo.course_offering"));
        DBCursor cur = courseOfferingCol.find();
        while(cur.hasNext()) {
            BasicDBObject obj = (BasicDBObject) cur.next();
            BasicDBObject facultyObj = (BasicDBObject)facultyCol.findOne(new BasicDBObject("id", obj.get("faculty_id")));
            BasicDBObject toAdd = new BasicDBObject("course_id", obj.get("course_id"))
                    .append("faculty_id", obj.get("faculty_id"))
                    .append("course_limit", obj.get("course_limit"))
                    .append("faculty_name", facultyObj.get("name"));

            AggregationOutput aggregationOutput = courseOfferingCol.aggregate(
                    new BasicDBObject("$match", new BasicDBObject("course_id", obj.get("course_id"))
                                                .append("faculty_id", obj.get("faculty_id"))),
                    new BasicDBObject("$project", new BasicDBObject("count",
                            new BasicDBObject("$size", "$students")))
            );

            Iterable<DBObject> list= aggregationOutput.results();

            if(list.iterator().hasNext()) {
                toAdd.append("course_registered", list.iterator().next().get("count"));
            } else {
                toAdd.append("course_registered", "---");

            }
            returnList.add(toAdd);
            Logger.info("Adding " + toAdd);
        }
        return ok(returnList.toString());
    }

    public static Result courseOfferingDetails() {
        Form<CourseOfferingForm> params = Form.form(CourseOfferingForm.class).bindFromRequest();
        if (params.hasErrors()) {
            Logger.error("Bad Request");
            return badRequest();
        }
        CourseOfferingForm formData = params.get();
        DB db = Application.acadDB();
        DBCollection col = db.getCollection(Play.application().configuration()
                .getString("mongo.students"));
        DBCollection courseOfferingCol = db.getCollection(Play.application().configuration().
                getString("mongo.course_offering"));
        BasicDBObject obj = (BasicDBObject) courseOfferingCol.findOne(
                new BasicDBObject("faculty_id", formData.faculty_id)
                .append("course_id", formData.course_id));
        if (obj == null) {
            Logger.warn("No entry for " + formData.course_id + " " + formData.faculty_id);
            return notFound();
        }

        BasicDBList retList = new BasicDBList();
        // Create an array of students and their names who are doing this course.
        ListIterator<Object> studentsList = ((BasicDBList) obj.get("students")).listIterator();
        while(studentsList.hasNext()) {
            Object nextItem = studentsList.next();
            Logger.info("Entry no "  + nextItem.toString());
            BasicDBObject studentObj = (BasicDBObject) col.findOne(
                    new BasicDBObject("entryno", nextItem.toString())
            );
            if (studentObj != null) {
                retList.add(new BasicDBObject("entryno", nextItem.toString())
                        .append("name", studentObj.get("name")));
            } else {
                retList.add(new BasicDBObject("entryno", nextItem.toString())
                .append("name", "---"));
            }
            Logger.info("Adding " + retList.toString());
        }
        return ok(retList.toString());
    }

    public static Result registerCourseOffering() {
        Form<RegisterCourseOffering> params = Form.form(RegisterCourseOffering.class).bindFromRequest();
        if (params.hasErrors()) {
            Logger.error("Bad Request");
            return badRequest();
        }
        RegisterCourseOffering formData = params.get();
        DB db = Application.acadDB();

        // Check if there is a course by the name.
        DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.course"));
        BasicDBObject courseObj = (BasicDBObject)col.findOne(new BasicDBObject("id", formData.course_id));
        BasicDBObject toRet = new BasicDBObject();
        if (courseObj == null) {
            toRet.append("error", "There is no such course.");
            Logger.error("No such course " + formData.course_id);
            return badRequest(toRet.toString());
        }

        // Check if faculty exists.
        DBCollection facultyCol = db.getCollection(Play.application().configuration().getString("mongo.faculty"));
        BasicDBObject facultyObj = (BasicDBObject)facultyCol.findOne(new BasicDBObject("id", formData.faculty_id));
        if (facultyObj == null) {
            toRet.append("error", "There is no such faculty.");
            Logger.error("No such faculty " + formData.faculty_id);
            return badRequest(toRet.toString());
        }

        DBCollection courseOfferingCol = db.getCollection(Play.application().configuration().
                getString("mongo.course_offering"));
        String emptyArray [] = {};
        courseOfferingCol.insert(new BasicDBObject("faculty_id", formData.faculty_id)
                .append("course_id", formData.course_id)
                .append("course_limit", formData.course_limit)
                .append("students", emptyArray));
        return ok();
    }

    public static Result registerStudent() {
        Form<RegisterStudentForm> params = Form.form(RegisterStudentForm.class).bindFromRequest();
        if (params.hasErrors()) {
            Logger.error("Bad Request");
            return badRequest();
        }
        RegisterStudentForm formData = params.get();
        String entryno = formData.entryno;
        String course_id = formData.course_id;

        // Confirm that the course is being offered.
        DB db = Application.acadDB();
        DBCollection col = db.getCollection(Play.application().configuration()
                .getString("mongo.students"));
        DBCollection courseOfferingCol = db.getCollection(Play.application().configuration()
        .getString("mongo.course_offering"));
        BasicDBObject courseObj = (BasicDBObject)courseOfferingCol.findOne(new BasicDBObject("course_id", course_id));
        BasicDBObject toRet = new BasicDBObject();
        if (courseObj == null) {
            toRet.append("error", "Course is not being offered.");
            Logger.error("Course not offered " + course_id);
            return badRequest(toRet.toString());
        }

        int studentLimit = Integer.parseInt(courseObj.get("course_limit").toString());
        int registeredStudents = 0;
        AggregationOutput aggregationOutput = courseOfferingCol.aggregate(
                new BasicDBObject("$match", new BasicDBObject("course_id", courseObj.get("course_id"))
                        .append("faculty_id", courseObj.get("faculty_id"))),
                new BasicDBObject("$project", new BasicDBObject("count",
                        new BasicDBObject("$size", "$students")))
        );

        Iterable<DBObject> list= aggregationOutput.results();

        if(list.iterator().hasNext()) {
            registeredStudents = Integer.parseInt(list.iterator().next().get("count").toString());
        }
        if (registeredStudents >= studentLimit)
            return ok("Student Limit reached");

        col.update(
                new BasicDBObject("entryno", entryno),
                new BasicDBObject("$push", new BasicDBObject("courses", course_id))
        );

        courseOfferingCol.update(
                new BasicDBObject("course_id", course_id),
                new BasicDBObject("$push", new BasicDBObject("students", entryno))
        );
        return ok();
    }
}
