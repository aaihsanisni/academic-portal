package service;

import com.mongodb.*;

import java.io.IOException;
import java.net.UnknownHostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import play.Logger;
import play.Play;

/**
 * Created by atul on 26/09/15.
 */
@Component
public class MongoQueryEngine {
    MongoClient mongoClient = null;
    DB acadDB = null;

    public MongoQueryEngine() throws IOException {

        try {
            mongoClient = new MongoClient(Play.application().configuration().
                    getString("mongo.address"), Play.application().configuration().getInt("mongo.port"));
        } catch (UnknownHostException e) {
            Logger.error("Unable to connect to the fucking database.");
        }

        acadDB = mongoClient.getDB("acad");
    }

    public DB getAcadDB() {
        return acadDB;
    }
}
