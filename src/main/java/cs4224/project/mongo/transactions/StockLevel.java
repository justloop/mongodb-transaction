package cs4224.project.mongo.transactions;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class StockLevel {
	
	/**
	 * Execute a stock level transaction.
	 * @param session
	 * @param w_id
	 * @param d_id
	 * @param t	Stock threshold
	 * @param l	Last L items to be examined
	 * @return
	 */
	public static boolean execute(MongoDatabase session, int w_id, int d_id, int t, int l) {
		FindIterable<Document> iterable = null;
		
		return true;
	}

}
