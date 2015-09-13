package controllers;

import java.net.UnknownHostException;

import play.*;
import play.mvc.*;

import views.html.*;
import akka.io.Tcp.Bind;

import com.mongodb.*;

public class Application extends Controller {

    public static DB acadDB() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(Play.application().configuration().
                    getString("mongo.address"), Play.application().configuration().getInt("mongo.port"));
        } catch (UnknownHostException e) {
            Logger.error("Unable to connect to the fucking database.");
            return null;
        }
        DB db = mongoClient.getDB("acad");
        return db;
    }

    public static Result index() {
        response().setContentType("text/html");
        return ok("<h1>Welcome to IITD Academic System</h1>");
    }

}
