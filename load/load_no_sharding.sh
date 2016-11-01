#!/bin/bash
DB=$1
echo "Create tables..."
/temp/mongodb-linux-x86_64-rhel70-3.2.9/bin/mongoimport --db ${DB} --collection warehouse <load-${DB}/mongodb/warehouse.json
/temp/mongodb-linux-x86_64-rhel70-3.2.9/bin/mongoimport --db ${DB} --collection district <load-${DB}/mongodb/district.json
/temp/mongodb-linux-x86_64-rhel70-3.2.9/bin/mongoimport --db ${DB} --collection item <load-${DB}/mongodb/item.json
/temp/mongodb-linux-x86_64-rhel70-3.2.9/bin/mongoimport --db ${DB} --collection order2 <load-${DB}/mongodb/order2.json
/temp/mongodb-linux-x86_64-rhel70-3.2.9/bin/mongoimport --db ${DB} --collection customer <load-${DB}/mongodb/customer.json

echo "Create index..."
/temp/mongodb-linux-x86_64-rhel70-3.2.9/bin/mongo < load-${DB}/index_no_sharding.js

echo "Done!"
