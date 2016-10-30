#!/bin/bash
echo "Starting config servers"
ssh xcnd9 "sh /temp/mongodb-linux-x86_64-rhel70-3.2.9/start_confgsvr.sh"
ssh xcnd10 "sh /temp/mongodb-linux-x86_64-rhel70-3.2.9/start_confgsvr.sh"
ssh xcnd11 "sh /temp/mongodb-linux-x86_64-rhel70-3.2.9/start_confgsvr.sh"
echo "Starting config servers done, waiting..."
sleep 10
echo "Starting mongos servers"
ssh xcnd9 "/temp/mongodb-linux-x86_64-rhel70-3.2.9/start_mongos.sh"
ssh xcnd10 "/temp/mongodb-linux-x86_64-rhel70-3.2.9/start_mongos.sh"
ssh xcnd11 "/temp/mongodb-linux-x86_64-rhel70-3.2.9/start_mongos.sh"
echo "Starting mongos servers done, waiting..."
sleep 10
echo "Starting replica set servers"
ssh xcnd9 "/temp/mongodb-linux-x86_64-rhel70-3.2.9/start_data.sh"
ssh xcnd10 "/temp/mongodb-linux-x86_64-rhel70-3.2.9/start_data.sh"
ssh xcnd11 "/temp/mongodb-linux-x86_64-rhel70-3.2.9/start_data.sh"
echo "Starting data servers done, waiting..."
