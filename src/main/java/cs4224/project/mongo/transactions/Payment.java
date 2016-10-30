package cs4224.project.mongo.transactions;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Payment {
	public static void main( String[] args ){
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		DB db = mongoClient.getDB("D8");
		System.out.println("Connect to database successfully"); 
		execute(db, 1,6,3,666.6);
		//insert_customdata(db);
		mongoClient.close();
	}
	/**
	 * Execute a payment transaction.
	 * @param session
	 * @param w_id
	 * @param d_id
	 * @param c_id
	 * @param payment
	 * @return
	 */
	public static boolean execute(DB db, int w_id, int d_id, int c_id,
								  double payment) {
		//update warehouse
		DBCollection warehouse = db.getCollection("warehouse");
		BasicDBObject query = new BasicDBObject("w_id", w_id);
		BasicDBObject updateQuery = new BasicDBObject().append("$inc",new BasicDBObject().append("w_ytd", payment));
		warehouse.update(query, updateQuery);
		
		//update district
		DBCollection district = db.getCollection("district");
		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject("w_id", w_id));
		list.add(new BasicDBObject("d_id", d_id));
		updateQuery = new BasicDBObject().append("$inc",new BasicDBObject().append("d_ytd", payment));
		district.update(new BasicDBObject("$and", list), updateQuery);
		
		//update customer
		DBCollection customer = db.getCollection("customer");
		BasicDBList list2 = new BasicDBList();
		list2 = new BasicDBList();
		list2.add(new BasicDBObject("w_id", w_id));
		list2.add(new BasicDBObject("d_id", d_id));
		list2.add(new BasicDBObject("c_id", c_id));
		BasicDBObject fields = new BasicDBObject().append("c_balance", payment * (-1))
												  .append("c_ytd_payment", payment)
												  .append("c_payment_cnt", 1);
		updateQuery = new BasicDBObject().append("$inc", fields);
		customer.update(new BasicDBObject("$and", list2), updateQuery);
		//select customer
		DBObject customer_o = customer.findOne(new BasicDBObject("$and", list2));
		print_result(customer_o, payment);
		return true;
	}
	
	public static void print_result(DBObject customer_o, double payment){
		System.out.println("Transaction 2: Payment");
		DBObject warehouse_o = (DBObject) customer_o.get("warehouse");
		DBObject district_o = (DBObject) customer_o.get("district");
		//1. Customer’s identifier (C W ID, C D ID, C ID), name (C FIRST, C MIDDLE, C LAST), 
		//address (C STREET 1, C STREET 2, C CITY, C STATE, C ZIP), C PHONE, C SINCE, C CREDIT, C CREDIT LIM, C DISCOUNT, C BALANCE
		//2. Warehouse’s address (W STREET 1, W STREET 2, W CITY, W STATE, W ZIP)
		//3. District’s address (D STREET 1, D STREET 2, D CITY, D STATE, D ZIP)
		//4. Payment amount PAYMENT
		System.out.println(customer_o.get("w_id") + "|" + customer_o.get("d_id") + "|" + customer_o.get("c_id") + "|" + 
		customer_o.get("c_first") + "|" + customer_o.get("c_middle") + "|" + customer_o.get("c_last") + "|" + 
				customer_o.get("c_street_1") + "|" + customer_o.get("c_street_2") + "|" + customer_o.get("c_city") + 
		"|" + customer_o.get("c_state") + "|" + customer_o.get("c_zip") + "|" + 
				customer_o.get("c_phone") + "|" + customer_o.get("c_since") + "|" + customer_o.get("c_credit") + "|" 
				+ customer_o.get("c_credit_lim") + "|" + customer_o.get("c_discount") + "|" + customer_o.get("c_balance") + "|"
				+ warehouse_o.get("w_street_1") + "|" + warehouse_o.get("w_street_2") + "|" + warehouse_o.get("w_city") + "|"
				+ warehouse_o.get("w_state") + "|" + warehouse_o.get("w_zip") + "|" + district_o.get("d_street_1") + "|"
				+ district_o.get("d_street_2") + "|" + district_o.get("d_city") + "|" + district_o.get("d_state") + "|" + 
				district_o.get("d_zip") + "|" + payment);
		

		
		
	}
}
