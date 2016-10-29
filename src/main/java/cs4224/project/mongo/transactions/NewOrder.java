package cs4224.project.mongo.transactions;

import com.mongodb.client.MongoDatabase;

public class NewOrder {

	/**
	 * Execute a new order request.
	 *
	 * @param session
	 * @param w_id
	 * @param d_id
	 * @param c_id
	 * @param numItems
	 * @param itemNum
	 * @param supplier
	 * @param quantity
	 * @return
	 */
	public static boolean execute(MongoDatabase session, int w_id, int d_id, int c_id,
								  int numItems, int[] itemNum, int[] supplier, int[] quantity) {
		//TODO impelement me

		return true;
	}
}
