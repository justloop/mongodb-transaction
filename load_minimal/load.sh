#!/bin/bash
DB=$1

MONGOIMPORT_PATH=mongoimport
MONGO_PATH=mongo

echo "Create tables..."
${MONGOIMPORT_PATH} --db ${DB} --collection warehouse <load-${DB}/mongodb/warehouse.json
${MONGOIMPORT_PATH} --db ${DB} --collection district <load-${DB}/mongodb/district.json
${MONGOIMPORT_PATH} --db ${DB} --collection item <load-${DB}/mongodb/item.json
${MONGOIMPORT_PATH} --db ${DB} --collection order2 <load-${DB}/mongodb/order2.json
${MONGOIMPORT_PATH} --db ${DB} --collection customer <load-${DB}/mongodb/customer.json

echo "Create index..."
${MONGO_PATH} < load-${DB}/index.js

echo "Done!"
