package cs4224.project.mongo.transactions;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class OrderStatus {
	
	/**
	 * Execute an order status transaction.
	 * @param session
	 * @param w_id
	 * @param d_id
	 * @param c_id
	 * @return
	 */
	public static boolean execute(MongoDatabase session, int w_id, int d_id, int c_id) {
		FindIterable<Document> iterable = null;
		
		// Find customer
		iterable = session.getCollection("customer")
			.find(
				and(eq("w_id", w_id), eq("d_id", d_id), eq("c_id", c_id))
			)
			.projection(fields(
				include("c_first", "c_middle", "c_last", "c_balance", "last_order_id"),
				exclude("_id")
			))
			.limit(1);
		Document customer = iterable.first();
		
		System.out.println("C_FIRST: " + customer.getString("c_first"));
		System.out.println("C_MIDDLE: " + customer.getString("c_middle"));
		System.out.println("C_LAST: " + customer.getString("c_last"));
		System.out.println("C_BALANCE: " + customer.getDouble("c_balance"));
		
		// Find customer's last order
		int o_id = customer.getInteger("last_order_id");
		iterable = session.getCollection("order2")
			.find(
				and(eq("o_w_id", w_id), eq("o_d_id", d_id), eq("o_id", o_id))
			)
			.limit(1);
		Document order = iterable.first();
		
		System.out.println("O_ID: " + o_id);
		System.out.println("O_ENTRY_D: " + order.getString("o_entry_d"));
		System.out.println("O_CARRIER_ID: " + order.getInteger("o_carrier_id"));
		
		String o_delivery_d = order.getString("ol_delivery_d");
		
		// List each ordered item
		@SuppressWarnings("unchecked")
		List<Document> orderlines = (List<Document>) order.get("orderlines");
		for (Document ol : orderlines) {
			System.out.println("OL_I_ID: " + ol.getInteger("ol_i_id"));
			System.out.println("OL_SUPPLY_W_ID: " + ol.getInteger("ol_supply_w_id"));
			System.out.println("OL_QUANTITY: " + ol.getInteger("ol_quantity"));
			System.out.println("OL_AMOUNT: " + ol.getDouble("ol_amount"));
			System.out.println("OL_DELIVERY_D: " + o_delivery_d);
		}
		
		return true;
	}

}
