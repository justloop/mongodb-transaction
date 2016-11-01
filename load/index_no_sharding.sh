#!/bin/bash
DB=$1
echo "Create index..."
/temp/mongodb-linux-x86_64-rhel70-3.2.9/bin/mongo < load-${DB}/index_no_sharding.js

echo "Done!"
