use D40;
db.warehouse.createIndex({w_id: 1});
db.district.createIndex({w_id:1, d_id:1});
db.customer.createIndex({w_id:1, d_id:1,c_id:1});
db.customer.createIndex({c_balance: -1});
db.order2.createIndex({o_id:-1 ,o_w_id:1, o_d_id:1});
db.order2.createIndex({o_carrier_id: 1});
db.item.createIndex({i_id: 1, i_w_id: 1});

sh.enableSharding("D40");
sh.shardCollection("D40.warehouse", { _id : 1 } )
sh.shardCollection("D40.district", { _id : 1 } )
sh.shardCollection("D40.customer", { _id : 1 } )
sh.shardCollection("D40.order2", { _id : 1 } )
sh.shardCollection("D40.item", { _id : 1 } )
