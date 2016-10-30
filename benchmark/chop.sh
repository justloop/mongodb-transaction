#!/bin/bash
FILES=$1-xact/*
for f in $FILES
do
	filename=$(basename $f)
	echo "Processing $filename file..."
	# take action on each file. $f store current file name
	head -n 10000 > $1-xact-10000/$filename
done
