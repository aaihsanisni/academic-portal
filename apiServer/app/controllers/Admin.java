package controllers;

import java.net.UnknownHostException;

import play.*;
import play.data.Form;
import play.data.DynamicForm;

import play.mvc.*;
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
    }

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
                    .append("course_registered", obj.get("course_registered"))
                    .append("course_limit", obj.get("course_limit"))
                    .append("faculty_name", facultyObj.get("name"));
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

        // Create an array of students and their names who are doing this course.

        Logger.info("Adding " + obj.toString());
        return ok(obj.toString());
    }
}
