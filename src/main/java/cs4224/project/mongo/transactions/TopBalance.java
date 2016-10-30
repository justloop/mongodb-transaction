package cs4224.project.mongo.transactions;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class TopBalance {

	public static void main( String[] args ){
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		DB db = mongoClient.getDB("D8");
		System.out.println("Connect to database successfully"); 
		execute(db);
		//insert_customdata(db);
		mongoClient.close();
	}
	
	/**
	 * Execute a top balance transaction.
	 * @param db
	 * @return
	 */
	public static boolean execute(DB db) {
		
		DBCollection customer = db.getCollection("customer");
		//DBCursor curr = customer.find();
		DBCursor curr = customer.find(
						new BasicDBObject())
						.sort(new BasicDBObject("c_balance", -1))
		       		.limit(10);
		System.out.println("Transaction 7: Top Balance:");
		while(curr.hasNext()) {
			//(a) Name of customer (C FIRST, C MIDDLE, C LAST)
			//(b) Balance of customer’s outstanding payment C BALANCE
			//(c) Warehouse name of customer W NAME 
			//(d) District name of customer D NAME
			DBObject temp = curr.next();
			String to_print = temp.get("c_first") + "|" + temp.get("c_middle") + "|" +temp.get("c_last") + "|" +
					temp.get("c_balance");
			if(temp.get("warehouse") != null)
				to_print = to_print + "|" + ((DBObject)temp.get("warehouse")).get("w_name");
			if (temp.get("district") != null)
				to_print = to_print + "|" + ((DBObject)temp.get("district")).get("d_name");
			System.out.println(to_print);
		}
		return true;
	}
	
    public static void insert_customdata(DB db){
    	DBCollection custom = db.getCollection("customer");
    	for(int i = 1; i < 20; i++){
        BasicDBObject temp = new BasicDBObject("_Id", new ObjectId()).
        	append("c_id", 100 + i).
        	append("c_delivery_cnt", 100).
        	append("c_balance", 1000 + i);
        custom.insert(temp);	
    	DBCursor curr = custom.find();
    	while(curr.hasNext()) {
			DBObject temp2 = curr.next();
			System.out.println(temp2);
		}
    	}
    }
}
