#!//usr/bin/python
from subprocess import Popen
import sys
from time import gmtime, strftime
import glob
import os

folder = strftime("%Y%m%d%H%M%S", gmtime())
if len(sys.argv) != 3:
    print "usage is: benchmark.py database clients_count."
    sys.exit()

if not os.path.exists(folder):
    os.makedirs(folder)
arguments = sys.argv
database = arguments[1]
print "Connecting to database: ", database
clientsCount = arguments[2]

print "Starting clients: ", clientsCount
commands = []
for i in range(int(clientsCount)):
    commands.append("sh test_run.sh " + str(i) + " > " +
                    folder + "/" + str(i) + ".txt")
procs = [Popen(c, shell=True, executable='/bin/bash') for c in commands]

print "Wait until program finishes..."
for p in procs:
    p.wait()

print "Collecting results..."
throughput = []
for filename in glob.glob(folder + "/*.txt"):
    last = ""
    for line in open(filename):
        last = line.strip()
    if len(last) == 0:
        print "Err reading output: ", filename
    else:
        lineArr = last.split(':')
        throughput.append(float(lineArr[1]))
    i = i + 1
max_value = max(throughput)
min_value = min(throughput)
avg_value = sum(throughput) / len(throughput)
print "\nMax throughput = \n", max_value
print "Min throughput = \n", min_value
print "Avg throughput = \n", avg_value
