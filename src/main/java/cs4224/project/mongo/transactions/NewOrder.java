package cs4224.project.mongo.transactions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class NewOrder {
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
		FindIterable<Document> iterable = null;
		
		// Reserve next order id
		Document district = session.getCollection("district").findOneAndUpdate(
			and(eq("w_id", w_id), eq("d_id", d_id)), 
			new Document("$inc", new Document("d_next_oid", 1))
		);
		
		int o_id = district.getInteger("d_next_oid", 1);
		
		// Update customer last order id
		Document customer = session.getCollection("customer").findOneAndUpdate(
			and(eq("w_id", w_id), eq("d_id", d_id), eq("c_id", c_id)), 
			new Document("$set", new Document("last_order_id", o_id))
		);
		
		String firstName = customer.getString("c_first");
		String middleName = customer.getString("c_middle");
		String lastName = customer.getString("c_last");
		String c_credit = customer.getString("c_credit");
		double c_discount = customer.getDouble("c_discount");
		double w_tax = ((Document) customer.get("warehouse")).getDouble("w_tax");
		double d_tax = ((Document) customer.get("district")).getDouble("d_tax");
		
		System.out.println("W_ID: " + w_id);
		System.out.println("D_ID: " + d_id);
		System.out.println("C_ID: " + c_id);
		System.out.println("C_LAST: " + lastName);
		System.out.println("C_CREDIT: " + c_credit);
		System.out.println("C_DISCOUNT: " + c_discount);
		
		System.out.println("W_TAX: " + w_tax);
		System.out.println("D_TAX: " + d_tax);
		
		// Find all items information
		List<Document> conditions = new ArrayList<>();
		for (int i = 0; i < numItems; i++) {
			conditions.add(new Document("i_w_id", supplier[i]).append("i_id", itemNum[i]));
		}
		iterable = session.getCollection("item")
			.find(new Document("$or", conditions))
			.projection(fields(
				include("i_w_id", "i_id", "i_name", "i_price", "s_quantity"),
				exclude("_id")
			)
		);
		
		Map<String, Document> itemStocks = new HashMap<>();
		iterable.forEach(new Block<Document>() {
		    @Override
		    public void apply(final Document item) {
		        itemStocks.put(item.getInteger("i_w_id") + ":" + item.getInteger("i_id"), item);
		    }
		});
		
		// All local ?
		int o_all_local = 0;
		// Total amount
		double totalAmount = 0.0;
		// Item stock bulk updates
		List<WriteModel<Document>> stockUpdates = new ArrayList<WriteModel<Document>>();
		// Order lines
		List<Document> orderlines = new ArrayList<>();
		
		// Process each item
		for (int i = 0; i < numItems; i++) {
			if (supplier[i] != w_id) o_all_local = 1;
			
			// Item stock
			Document item = itemStocks.get(supplier[i] + ":" + itemNum[i]);
			String itemName = item.getString("i_name");
			int s_quantity = item.getInteger("s_quantity");
			double i_price = item.getDouble("i_price");
			
			double itemAmount = quantity[i] * i_price;
			totalAmount += itemAmount;
			
			// Update stock information
			int s_adjust = s_quantity - quantity[i] < 10 
					? 100 - quantity[i] : 0 - quantity[i];
			int is_remote = supplier[i] != w_id ? 1 : 0;
			
			stockUpdates.add(new UpdateOneModel<Document>(
				and(eq("i_w_id", supplier[i]), eq("i_id", itemNum[i])),
				new Document("$inc", new Document("s_quantity", s_adjust)
					.append("s_ytd", quantity[i]).append("s_order_cnt", 1)
					.append("s_remote_cnt", is_remote))
			));
			
			// Create order line
			orderlines.add(new Document("ol_i_id", itemNum[i])
				.append("ol_i_name", itemName).append("ol_amount", itemAmount)
				.append("ol_supply_w_id", supplier[i]).append("ol_quantity", quantity[i])
				.append("ol_dist_info", ""));
			
			System.out.println("ITEM_NUMBER: " + itemNum[i]);
			System.out.println("I_NAME: " + itemName);
			System.out.println("SUPPLIER_WARHOUSE: " + supplier[i]);
			System.out.println("QUANTITY: " + quantity[i]);
			System.out.println("OL_AMOUNT: " + itemAmount);
			System.out.println("S_QUANTITY: " + s_quantity);
		}
		
		// Calculate total amount
		totalAmount = totalAmount * (1 + d_tax + w_tax) * (1 - c_discount);
		
		// Bulk update item stocks
		session.getCollection("item").bulkWrite(stockUpdates);
		
		// Create and insert new order
		String o_entry_d = df.format(new Date());
		Document order = new Document("o_w_id", w_id)
			.append("o_d_id", d_id).append("o_id", o_id).append("o_c_id", c_id)
			.append("customer", new Document("c_first", firstName)
				.append("c_middle", middleName).append("c_last", lastName))
			.append("o_carrier_id", -1).append("o_ol_cnt", numItems)
			.append("o_all_local", o_all_local).append("o_entry_d", o_entry_d)
			.append("orderlines", orderlines);
		
		session.getCollection("order2").insertOne(order);
		
		System.out.println("O_ID: " + o_id);
		System.out.println("O_ENTRY_D: " + o_entry_d);
		System.out.println("NUM_ITEMS: " + numItems);
		System.out.println("TOTAL_AMOUNT: " + totalAmount);

		return true;
	}
}
