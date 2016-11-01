package cs4224.project.mongo.transactions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class StockLevel {
	
	/**
	 * Execute a stock level transaction.
	 * @param session
	 * @param w_id
	 * @param d_id
	 * @param t	Stock threshold
	 * @param l	Last L orders to be examined
	 * @return
	 */
	public static boolean execute(MongoDatabase session, int w_id, int d_id, 
			int t, int l) {
		FindIterable<Document> iterable = null;
		
		// Find next available order id
		Document district = session.getCollection("district")
			.find(
				and(eq("w_id", w_id), eq("d_id", d_id))
			)
			.first();
		
		int d_next_oid = district.getInteger("d_next_oid");
		
		// Find last L orders
		iterable = session.getCollection("order2")
			.find(
				and(eq("o_w_id", w_id), eq("o_d_id", d_id),
						gte("o_id", d_next_oid - l), lt("o_id", d_next_oid))
			)
			.projection(fields(
				include("orderlines"), exclude("_id")
			));
		
		// Collect all appeared items
		final Set<Integer> items = new HashSet<>();
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document order) {
		    	@SuppressWarnings("unchecked")
				List<Document> orderlines = (List<Document>) order.get("orderlines");
				for (Document ol : orderlines) {
					items.add(ol.getInteger("ol_i_id"));
				}
		    }
		});
		
		// Find stock level
		long number = session.getCollection("item")
			.count(
				and(eq("i_w_id", w_id), in("i_id", items), lt("s_quantity", t))
			);
		
		System.out.println("Total number of items where stock is lower than threshould: " 
				+ number);
		
		return true;
	}

}
