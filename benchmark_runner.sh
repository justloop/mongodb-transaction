#!/bin/bash
echo "start benchmark runner"

for data in "D8" "D40"
do
	for clients in 10 20 40
	do
		echo "cd ../benchmark ; python benchmark.py ${data} ${clients} > benchmark-${data}-${clients}.log"
		cd ../benchmark ; python benchmark.py ${data} ${clients} 2>&1 > benchmark-${data}-${clients}.log
	done
done
