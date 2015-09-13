package controllers;

import java.net.UnknownHostException;

import play.*;
import play.data.Form;
import play.data.DynamicForm;

import play.mvc.*;
import views.html.*;
import akka.io.Tcp.Bind;
import play.data.validation.Constraints;

import com.mongodb.*;


/**
 * Created by atul on 13/09/15.
 */
public class Course extends Controller {
    public static class AddForm {
        public String id;
        public String name;
        public String dept;
    };

    public static class ViewCourseForm {
        public String id;
    }

    public static class DeleteForm {
        public String id;
    }
    public static Result view() {
        BasicDBList returnList = new BasicDBList();
        DB db = Application.acadDB();
        DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.course"));
        DBCursor cur = col.find();
        while(cur.hasNext()) {
            BasicDBObject obj = (BasicDBObject) cur.next();
            BasicDBObject toAdd = new BasicDBObject("id", obj.get("id"))
                    .append("name", obj.get("name"))
                    .append("dept", obj.get("dept"));
            returnList.add(toAdd);
            Logger.info("Adding " + toAdd);
        }
        return ok(returnList.toString());
    }

    public static Result viewCourse() {
        Form<ViewCourseForm> params = Form.form(ViewCourseForm.class).bindFromRequest();
        if (params.hasErrors()) {
            Logger.error("Bad Request");
            return badRequest();
        }
        ViewCourseForm formData = params.get();
        String facultyID = formData.id;

        DB db =Application.acadDB();
        DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.course"));
        DBCursor cursor = col.find(new BasicDBObject("id", facultyID));
        BasicDBList retList = new BasicDBList();
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject) cursor.next();
            retList.add(new BasicDBObject("id", obj.get("id"))
                    .append("name", obj.get("name"))
                    .append("dept", obj.get("dept")));
        }

        return ok(retList.toString());

    }

    public static Result add() {
        Form<AddForm> params = Form.form(AddForm.class).bindFromRequest();
        if (params.hasErrors()) {
            Logger.error("Bad Request");
            return badRequest();
        }
        AddForm addData = params.get();
        Logger.info("id" + addData.id + "Name:" + addData.name + " Dept:" + addData.dept);

        DB db = Application.acadDB();
        DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.course"));
        BasicDBObject obj = new BasicDBObject().append("id", addData.id)
                .append("name", addData.name)
                .append("dept", addData.dept);
        //check entry before inserting...
        col.insert(obj);

        // Return all the items that have been added.
        return ok();
    }

    public static Result delete() {
        Form<DeleteForm> params = Form.form(DeleteForm.class).bindFromRequest();
        if (params.hasErrors()) {
            Logger.error("Bad Request");
            return badRequest();
        }
        DeleteForm formData = params.get();
        String studentID = formData.id;

        DB db = Application.acadDB();
        DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.course"));
        col.remove(new BasicDBObject("id",studentID));
        return ok("Success");
    }

    // ID and the field to be updated.
    public static Result update() {
        return ok("Update");
    }

}
