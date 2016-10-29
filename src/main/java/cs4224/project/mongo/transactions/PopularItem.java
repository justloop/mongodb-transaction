package cs4224.project.mongo.transactions;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class PopularItem {

	/**
	 * Execute a popular item transaction.
	 * @param session
	 * @param w_id
	 * @param d_id
	 * @param l	Number of last orders to be examined.
	 * @return
	 */
	public static boolean execute(MongoDatabase session, int w_id, int d_id, int l) {
		// TODO implement me

		return true;
	}

}
