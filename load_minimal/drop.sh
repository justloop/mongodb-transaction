#!/bin/bash
DB=$1
mongo < load-${DB}/drop.js
