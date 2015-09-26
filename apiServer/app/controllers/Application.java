package controllers;

import java.net.UnknownHostException;


import play.mvc.*;
import play.Play;
import play.Logger;

import service.MongoQueryEngine;
import views.html.*;
import akka.io.Tcp.Bind;

import com.mongodb.*;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class Application extends Controller {
    @Autowired
    private static MongoQueryEngine mqe;

    public Application() {
    }

    public static DB acadDB() {
        /*
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Application.class);
        MongoQueryEngine mqe = context.getBean(MongoQueryEngine.class);
        return mqe.getAcadDB();
        */
        MongoClient mongoClient = null;

        try {
            mongoClient = new MongoClient(Play.application().configuration().
                    getString("mongo.address"), Play.application().configuration().getInt("mongo.port"));
        } catch (UnknownHostException e) {
            Logger.error("Unable to connect to the fucking database.");
        }
        return mongoClient.getDB("acad");
    }

    public static Result index() {
        response().setContentType("text/html");
        return ok("<h1>Welcome to IITD Academic System</h1>");
    }

}
