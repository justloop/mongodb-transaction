#!/bin/bash
kill -2 `cat /temp/mongodb-linux-x86_64-rhel70-3.2.9/configsvr.pid`
echo "MongoDB config server stop successfully"
kill -2 `cat /temp/mongodb-linux-x86_64-rhel70-3.2.9/mongos.pid`
echo "MongoDB mongos server stop successfully"
kill -2 `cat /temp/mongodb-linux-x86_64-rhel70-3.2.9/set10.pid`
echo "MongoDB set10 stop successfully"

