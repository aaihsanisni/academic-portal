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
//import com.mongodb.client.MongoDatabase;

// TODO Create only one instance of mongoclient in a different class.
public class Students extends Controller {
	
	public static class AddForm {
		public String entryno;
		public String name;
		public String dept;
	};
	
	public static class ViewStudentForm {
		public String entryno;
	}

	public static class DeleteForm {
		public String entryno;
	}
	// Returns all the students and their details
    public static Result view() {
    	BasicDBList returnList = new BasicDBList();
    	DB db = Application.acadDB();
    	DBCollection studentCol = db.getCollection(Play.application().configuration().getString("mongo.students"));
    	DBCursor cur = studentCol.find();
    	while(cur.hasNext()) {
    		BasicDBObject obj = (BasicDBObject) cur.next();
    		BasicDBObject toAdd = new BasicDBObject("entryno", obj.get("entryno"))
    				.append("name", obj.get("name"))
					.append("dept", obj.get("dept"))
					.append("courses", obj.get("courses"));
    		returnList.add(toAdd);
    		Logger.info("Adding " + toAdd);
    	}
    	return ok(returnList.toString());
    }
    
    public static Result viewStudent() {
    	Form<ViewStudentForm> params = Form.form(ViewStudentForm.class).bindFromRequest();
    	if (params.hasErrors()) {
    		Logger.error("Bad Request");
    		return badRequest();
    	}
    	ViewStudentForm formData = params.get();
    	String studentID = formData.entryno;

    	DB db =Application.acadDB();
    	DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.students"));
    	DBCursor cursor = col.find(new BasicDBObject("entryno", studentID));
    	BasicDBList retList = new BasicDBList();

    	while(cursor.hasNext()) {
    		BasicDBObject obj = (BasicDBObject) cursor.next();
    		retList.add(new BasicDBObject("entryno", obj.get("entryno"))
					.append("name", obj.get("name"))
    				.append("dept", obj.get("dept"))
					.append("courses", obj.get("courses")));
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
    	Logger.info("Entry" + addData.entryno + "Name:" + addData.name + " Dept:" + addData.dept);
    	
    	DB db = Application.acadDB();
    	DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.students"));
		String emptyArray [] = {};
		BasicDBObject obj = new BasicDBObject().append("entryno", addData.entryno)
				.append("name", addData.name)
    			.append("dept", addData.dept)
				.append("courses", emptyArray);
    	col.insert(obj);
    	return ok();
    }

    public static Result delete() {
    	Form<DeleteForm> params = Form.form(DeleteForm.class).bindFromRequest();
    	if (params.hasErrors()) {
    		Logger.error("Bad Request");
    		return badRequest();
    	}
    	DeleteForm formData = params.get();
    	String studentID = formData.entryno;
    	
    	DB db = Application.acadDB();
    	DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.students"));
    	col.remove(new BasicDBObject("entryno",studentID));
    	return ok("Success");
    }
    
    // ID and the field to be updated.
    public static Result update() {
    	return ok("Update");
    }

}