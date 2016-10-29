package cs4224.mongo_test;

import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        System.out.println( "Hello World!" );
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        try{
            // Now connect to your databases
            DB db2 = mongoClient.getDB("test");
            System.out.println("Connect to database successfully");       
            DBCollection coll = db2.getCollection("mycol");
            System.out.println("Collection mycol selected successfully");
   			
            BasicDBObject doc = new BasicDBObject("title", "MongoDB").
               append("description", "database").
               append("likes", 100).
               append("url", "http://www.tutorialspoint.com/mongodb/").
               append("by", "tutorials point");
   				
            coll.insert(doc);
            System.out.println("Document inserted successfully");
         }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
         }finally{
        	 mongoClient.close();
         }

    }
}
