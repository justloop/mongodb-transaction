#!/bin/bash
DB=$1
/temp/mongodb-linux-x86_64-rhel70-3.2.9/bin/mongo < load-${DB}/drop.js
