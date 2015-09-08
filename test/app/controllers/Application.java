package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        response().setContentType("text/html");
        return ok("<h1>Welcome to IITD Academic System</h1>");
    }

}
