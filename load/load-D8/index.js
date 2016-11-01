use D8;
db.warehouse.createIndex({w_id: 1});
db.district.createIndex({w_id:1, d_id:1});
db.customer.createIndex({w_id:1, d_id:1,c_id:1});
db.customer.createIndex({c_balance: -1});
db.order2.createIndex({o_id:-1 ,o_w_id:1, o_d_id:1});
db.order2.createIndex({o_carrier_id: 1});
db.item.createIndex({i_id: 1, i_w_id: 1});

sh.enableSharding("D8");
sh.shardCollection("D8.warehouse", { w_id : 1 } )
sh.shardCollection("D8.district", { w_id : 1} )
sh.shardCollection("D8.customer", { w_id : 1} )
sh.shardCollection("D8.order2", { o_id : "hashed"} )
sh.shardCollection("D8.item", { i_id : 1} )
