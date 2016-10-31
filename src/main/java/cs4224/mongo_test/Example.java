package cs4224.mongo_test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

public class Example {
	
	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase db = mongoClient.getDatabase("mydb");

		FindIterable<Document> iterable;
		
		iterable = db.getCollection("mycol").find(
			in("age", Arrays.asList(23, 24))
		);
		
		/*
		Document result = db.getCollection("mycol").findOneAndUpdate(
			eq("name", "name1"), 
			//new Document("$set", new Document("age", 33).append("grade", 10))
			new Document("$inc", new Document("age", -1).append("grade", 1))
		);
		*/
		
		List<WriteModel<Document>> updates = new ArrayList<WriteModel<Document>>();
		updates.add(new UpdateOneModel<Document>(
			eq("name", "name1"), 
			//new Document("$set", new Document("age", 33).append("grade", 10))
			new Document("$inc", new Document("age", -1).append("grade", 1))
		));
		
		updates.add(new UpdateOneModel<Document>(
			eq("name", "name2"), 
			//new Document("$set", new Document("age", 33).append("grade", 10))
			new Document("$inc", new Document("age", -1).append("grade", 1))
		));
		
		BulkWriteResult result = db.getCollection("mycol").bulkWrite(updates);
		
		System.out.println(result);
		
		/*
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document document) {
		        System.out.println(document);
		    }
		});
		*/
		
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
		
		/*
		// Find by =
		iterable = db.getCollection("restaurants")
				.find(new Document("borough", "Manhattan"))
				.projection(fields(include("name"), exclude("_id")))
				.limit(1);
		
		//db.getCollection("restaurants").find(eq("borough", "Manhattan"));
		Document first = iterable.first();
		System.out.println(first);
		
		System.out.println(first.getString("name"));
		*/
		
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
