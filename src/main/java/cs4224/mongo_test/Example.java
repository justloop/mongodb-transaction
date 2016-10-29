package cs4224.mongo_test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

public class Example {
	
	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("mydb");

		FindIterable<Document> iterable;
		
		/*
		// Find one
		FindIterable<Document> iterable = db.getCollection("restaurants").find().limit(1);
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.out.println(document);
		    }
		});
		*/
		
		// Find by =
		iterable = db.getCollection("restaurants")
				.find(new Document("borough", "Manhattan"))
				.projection(fields(include("name"), exclude("_id")))
				.limit(1);
		
		//db.getCollection("restaurants").find(eq("borough", "Manhattan"));
		Document first = iterable.first();
		System.out.println(first);
		
		System.out.println(first.getString("name"));
		
		/*
		db.getCollection("restaurants").find(gt("grades.score", 30));
		
		iterable = db.getCollection("restaurants").find(
		        new Document("cuisine", "Italian").append("address.zipcode", "10075"));
		
		db.getCollection("restaurants").find(and(eq("cuisine", "Italian"), 
				eq("address.zipcode", "10075")));
		
		iterable = db.getCollection("restaurants").find(
		        new Document("$or", asList(new Document("cuisine", "Italian"),
		                new Document("address.zipcode", "10075"))));
		
		db.getCollection("restaurants").find(or(eq("cuisine", "Italian"), 
				eq("address.zipcode", "10075")));
		*/
	}

}
