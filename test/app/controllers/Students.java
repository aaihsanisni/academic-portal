package controllers;

import java.net.UnknownHostException;

import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import akka.io.Tcp.Bind;

import com.mongodb.*;
//import com.mongodb.client.MongoDatabase;

// TODO Create only one instance of mongoclient in a different class.
public class Students extends Controller {
	
	public static class AddForm {
		public String name;
		public String dept;
	};
	
	public static class ViewStudentForm {
		public String name;
	}
	
	// Returns all the students and their details
    public static Result view() {
    	MongoClient mongoClient = null;
    	try {
			mongoClient = new MongoClient(Play.application().configuration().
					getString("mongo.address"), Play.application().configuration().getInt("mongo.port"));
		} catch (UnknownHostException e) {
			Logger.error("Unable to connect to the fucking database.");
		//	e.printStackTrace(e);
		}
    	BasicDBList returnList = new BasicDBList();
    	DB db = mongoClient.getDB("acad");
    	DBCollection studentCol = db.getCollection(Play.application().configuration().getString("mongo.students"));
    	DBCursor cur = studentCol.find();
    	while(cur.hasNext()) {
    		BasicDBObject obj = (BasicDBObject) cur.next();
    		BasicDBObject toAdd = new BasicDBObject("name", obj.get("name"))
    			.append("dept", obj.get("dept"));
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
    	String studentID = formData.name;
    	
    	MongoClient mongoclient = null;
    	try {
			mongoclient = new MongoClient(Play.application().configuration().getString("mongo.address"),
					Play.application().configuration().getInt("mongo.port"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	DB db = mongoclient.getDB("acad");
    	DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.students"));
    	DBCursor cursor = col.find(new BasicDBObject("name", studentID));
    	BasicDBList retList = new BasicDBList();
    	while(cursor.hasNext()) {
    		BasicDBObject obj = (BasicDBObject) cursor.next();
    		retList.add(new BasicDBObject("name", obj.get("name"))
    				.append("dept", obj.get("dept")));
    	}
    	
    	return ok(retList.toString());
    	
    }
    
    // Params: name and dept
    public static Result add() {
    	Form<AddForm> params = Form.form(AddForm.class).bindFromRequest();
    	
    	if (params.hasErrors()) {
    		Logger.error("Bad Request");
    	    return badRequest();
    	}
    	AddForm addData = params.get();
    	Logger.info("Name:" + addData.name + " Dept:" + addData.dept);
    	
    	MongoClient mongoclient = null;
    	try {
			mongoclient = new MongoClient(Play.application().configuration().getString("mongo.address"),
					Play.application().configuration().getInt("mongo.port"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	DB db = mongoclient.getDB("acad");
    	DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.students"));
    	BasicDBObject obj = new BasicDBObject().append("name", addData.name).
    			append("dept", addData.dept);
    	//check entry before inserting...
    	col.insert(obj);
    	return ok();
    }
    
    public static Result delete() {
    	Form<ViewStudentForm> params = Form.form(ViewStudentForm.class).bindFromRequest();
    	if (params.hasErrors()) {
    		Logger.error("Bad Request");
    		return badRequest();
    	}
    	ViewStudentForm formData = params.get();
    	String studentID = formData.name;
    	
    	MongoClient mongoclient = null;
    	try {
			mongoclient = new MongoClient(Play.application().configuration().getString("mongo.address"),
					Play.application().configuration().getInt("mongo.port"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	DB db = mongoclient.getDB("acad");
    	DBCollection col = db.getCollection(Play.application().configuration().getString("mongo.students"));
    	col.remove(new BasicDBObject("name",studentID));
    	return ok("Success");
    }
    
    // ID and the field to be updated.
    public static Result update() {
    	return ok("Update");
    }

}
