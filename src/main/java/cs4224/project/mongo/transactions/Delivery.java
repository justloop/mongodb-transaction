package cs4224.project.mongo.transactions;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Delivery {
	
	public static void main( String[] args ){
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		DB db = mongoClient.getDB("D8");
		System.out.println("Connect to database successfully");  
		//update into some diff value
		execute(db, 1, 888);
		//update_all_order(db);
		//insert_customdata(db);
		//print_all(db);
		mongoClient.close();
	}
	
	public static void update_all_order(DB db){
		DBCollection order2 = db.getCollection("order2");
		DBCursor curr = order2.find();
		while(curr.hasNext()) {
			DBObject temp2 = curr.next();
			System.out.println(temp2.get("_id") + "|" + temp2.get("o_id") + "|" + temp2.get("o_w_id") + "|" + temp2.get("o_carrier_id"));
			BasicDBObject query = new BasicDBObject("_id", new ObjectId(temp2.get("_id").toString()));
			order2.update(
					query,
					  new BasicDBObject(
					    "$set",
					    new BasicDBObject("o_carrier_id", -1)
					));
			order2.update(
					query,
					  new BasicDBObject(
					    "$set",
					    new BasicDBObject("o_c_id", 100)
					));
        }
	}
	
	public static boolean populate_orderlines(DB db, int w_id, int d_id){
		
		System.out.println("populating orderlines of: w_id: " + w_id + "  d_id: " + d_id);
		BasicDBList list2 = new BasicDBList();
		list2.add(new BasicDBObject("o_d_id", d_id));
		list2.add(new BasicDBObject("o_w_id", w_id));
		
		DBCollection order2 = db.getCollection("order2");
		BasicDBObject orderline = new BasicDBObject();
		orderline.put("ol_amount", 111.1);
		orderline.put("ol_quantity", 12);
		orderline.put("ol_i_id", 100);
		BasicDBObject update = new BasicDBObject();
		update.put("$push", new BasicDBObject("orderlines",orderline));
		order2.update(new BasicDBObject("$and", list2), update,true,true);
		
		BasicDBObject orderline2 = new BasicDBObject();
		orderline2 = new BasicDBObject();
		orderline2.put("ol_amount", 222.1);
		orderline2.put("ol_quantity", 13);
		orderline2.put("ol_i_id", 123);
		update = new BasicDBObject();
		update.put("$push", new BasicDBObject("orderlines",orderline2));
		order2.update(new BasicDBObject("$and", list2), update,true,true);
		return true;
	}
	
    public static boolean execute(DB db, int w_id, int o_carrier_id){
        try{
        	DBCollection order2 = db.getCollection("order2");
        	// Let N denote the value of the smallest order number O_ID for district (W_ID,DISTRICT_NO) with O_CARRIER_ID = null;
        	for(int i = 1; i <= 10; i++){
        		//populate_orderlines(db, w_id, i);
        		BasicDBList list = new BasicDBList();
        		list.add(new BasicDBObject("o_d_id", i));
        		list.add(new BasicDBObject("o_w_id", w_id));
        		list.add(new BasicDBObject("o_carrier_id", -1));
        		DBCursor curr = order2.find(
        				new BasicDBObject("$and", list))
        				.sort(new BasicDBObject("o_id", 1))
                		.limit(1);
        		if (curr.hasNext()){
        			DBObject temp = curr.next();
        			BasicDBObject query = new BasicDBObject("_id", new ObjectId(temp.get("_id").toString()));
        			BasicDBObject updateFields = new BasicDBObject();
        			// update o_carrier_id in order
        			// update order_line's date time
        			updateFields.append("o_carrier_id", o_carrier_id);
        			updateFields.append("ol_delivery_d", (new Date()).toString());
        			BasicDBObject setQuery = new BasicDBObject();
        			setQuery.append("$set", updateFields);
        			order2.update(query, setQuery);
        			double sum = 0;
        			ArrayList<BasicDBObject> orderlines= (ArrayList<BasicDBObject>) temp.get("orderlines");
        			for(BasicDBObject line: orderlines){
        				sum += new Double(line.get("ol_amount").toString());
        			}
        			System.out.println("Sum of all objects: " + sum);
        			// update customer
        			//Increment C BALANCE by B, where B denote the sum of OL AMOUNT for all the items placed in order X, Increment C DELIVERY CNT by 1
        			DBCollection customer = db.getCollection("customer");
            		BasicDBList list2 = new BasicDBList();
            		list2.add(new BasicDBObject("d_id", i));
            		list2.add(new BasicDBObject("w_id", w_id));
            		list2.add(new BasicDBObject("c_id", Integer.valueOf((temp.get("o_c_id").toString()))));
        			System.out.println("Upadating customer: " + list2);
        			BasicDBObject fields = new BasicDBObject().append("c_delivery_cnt", 1)
							  .append("c_balance", sum);
        			BasicDBObject updateQuery = new BasicDBObject().append("$inc", fields);
        			customer.update(new BasicDBObject("$and", list2), updateQuery);
        		}
        	}
        	return true;
         }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage());
			return false;
         }
    }
    
    public static void print_all(DB db){
    	DBCollection order2 = db.getCollection("order2");
        BasicDBObject temp = new BasicDBObject("o_w_id", 1).
            	append("o_carrier_id", -1);
        
    	DBCursor curr3 = order2.find(temp);
		while(curr3.hasNext()) {
			DBObject temp2 = curr3.next();
			System.out.println(temp2);
		}
    }
    
    public static void insert_testdata(DBCollection order){
    	for (int i = 0; i < 10; i++){
        	BasicDBObject temp = new BasicDBObject("_Id", new ObjectId()).
        			append("o_w_id", 1).
        			append("o_d_id", 1).
        			append("o_id", 123 + i).
        			append("o_c_id", 123 + i).
        			append("o_carrier_id", -1);
        	order.insert(temp);
        }
    }
    
    public static void insert_customdata(DB db){
    	DBCollection custom = db.getCollection("customer");
        BasicDBObject temp = new BasicDBObject("_Id", new ObjectId()).
        	append("c_id", 100).
        	append("c_delivery_cnt", 100).
        	append("c_balance", 1000);
        custom.insert(temp);	
    	DBCursor curr = custom.find();
    	while(curr.hasNext()) {
			DBObject temp2 = curr.next();
			System.out.println(temp2);
		}
    }     
}
