package cs4224.project.mongo.transactions;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.ascending;
import static java.util.Arrays.asList;

public class WMTester {
	
	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoDatabase session = mongoClient.getDatabase("D8");

		// Test order status
		//OrderStatus.execute(session, 1, 1, 1);
		
		// Test stock level
		//StockLevel.execute(session, 1, 1, 100, 5);
		
		// Test popular item
		PopularItem.execute(session, 2, 2, 10);
	}

}
