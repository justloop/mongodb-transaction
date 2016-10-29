package cs4224.project.mongo.transactions;

import com.mongodb.client.MongoDatabase;

public class Payment {

	/**
	 * Execute a payment transaction.
	 * @param session
	 * @param w_id
	 * @param d_id
	 * @param c_id
	 * @param payment
	 * @return
	 */
	public static boolean execute(MongoDatabase session, int w_id, int d_id, int c_id,
								  double payment) {
		//TODO impelement me

		return true;
	}
}
