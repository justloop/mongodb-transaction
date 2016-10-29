#!/bin/bash
DB=$1
echo "Create tables..."
mongoimport --db ${DB} --collection warehouse <load-${DB}/mongodb/warehouse.json
mongoimport --db ${DB} --collection district <load-${DB}/mongodb/district.json
mongoimport --db ${DB} --collection item <load-${DB}/mongodb/item.json
mongoimport --db ${DB} --collection order2 <load-${DB}/mongodb/order2.json
mongoimport --db ${DB} --collection customer <load-${DB}/mongodb/customer.json

echo "Create index..."
mongo < load-${DB}/index.js

echo "Done!"
