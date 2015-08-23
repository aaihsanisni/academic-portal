package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Students extends Controller {

    public static Result view() {
    	return ok("view");
    }
    
    public static Result viewStudent(String studentID) {
    	return ok("View Student");
    }
    
    public static Result add() {
    	return ok("Add");
    }
    
    public static Result delete() {
    	return ok("Delete");
    }
    
    public static Result update() {
    	return ok("Update");
    }

}
