#!/bin/bash
DB=$1

MONGO_PATH=mongo

${MONGO_PATH} < load-${DB}/drop.js
