package cs4224.project.mongo.transactions;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.Block;
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
		FindIterable<Document> iterable = null;
		
		System.out.println("W_ID: " + w_id);
		System.out.println("D_ID: " + d_id);
		System.out.println("Number of last orders to be examined: " + l);
		
		// Find next available order id
		iterable = session.getCollection("district")
			.find(
				and(eq("w_id", w_id), eq("d_id", d_id))
			)
			.limit(1);
		
		Document district = iterable.first();
		int d_next_oid = district.getInteger("d_next_oid");
		
		// Find last L orders
		iterable = session.getCollection("order2")
			.find(
				and(eq("o_w_id", w_id), eq("o_d_id", d_id),
						gte("o_id", d_next_oid - l), lt("o_id", d_next_oid))
			);
		
		// Distinct popular items
		Set<String> popItems = new HashSet<>();
		// All items ordered in each order
		List<Set<String>> orderItems = new ArrayList<>();
		
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document order) {
		    	System.out.println("O_ID: " + order.getInteger("o_id"));
				System.out.println("O_ENTRY_D: " + order.getString("o_entry_d"));
				
				Document customer = (Document) order.get("customer");
				System.out.println("C_FIRST: " + customer.getString("c_first"));
				System.out.println("C_MIDDLE: " + customer.getString("c_middle"));
				System.out.println("C_LAST: " + customer.getString("c_last"));
		        
				// Find most popular items in this order
				int max = 0;
				Set<String> items = new HashSet<>();
				// All ordered items
				Set<String> allItems = new HashSet<>();
				
		    	@SuppressWarnings("unchecked")
				List<Document> orderlines = (List<Document>) order.get("orderlines");
				for (Document ol : orderlines) {
					String ol_i_name = ol.getString("ol_i_name");
					allItems.add(ol_i_name);
					
					int quantity = ol.getInteger("ol_quantity");
					if (quantity > max) {
						max = quantity;
						items.clear();
						items.add(ol_i_name);
					} else if (quantity == max) {
						items.add(ol_i_name);
					}
				}
				
				for (String item : items) {
					System.out.println("I_NAME: " + item);
					System.out.println("OL_QUANTITY: " + max);
				}
				
				popItems.addAll(items);
				orderItems.add(allItems);
		    }
		});
		
		// The percentage of examined orders that contains each popular item
		Map<String, Integer> counterMap = new HashMap<>();
		for (String pop : popItems) {
			for (Set<String> items : orderItems) {
				if (items.contains(pop)) {
					counterMap.put(pop, counterMap.containsKey(pop) 
							? counterMap.get(pop) + 1 : 1);
				}
			}
		}
		
		for (Map.Entry<String, Integer> entry : counterMap.entrySet()) {
			System.out.println("I_NAME: " + entry.getKey());
			float percentage = entry.getValue() * 100.0f / orderItems.size();
			System.out.println("Percentage: " + percentage + "%");
		}

		return true;
	}

}
