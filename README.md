# MongoDB transaction

## Code structure
Code can be open with Intellij or Eclipse with Java8 (lambda level) and Maven3
```
├── load: the scripts to load/drop database, generate loadable json files from original data, load data into mongodb
    ├── load.sh: script to load the generated data into mongodb, usage: sh load.sh D8|D40
    ├── drop.sh: script to drop the database in mongodb, usage: sh drop.sh D8|D40
    ├── index.sh: script to add defined index to mongodb, usage: sh index.sh D8|D40
    ├── load-D8|D40: contains the scripts to generate json files from original data
        ├── gen.sh: combined script to generate the loadable data, which invokes all the py files
        ├── schema: the original data csv schema, we put this header to all the original data
        ├── D8|D40-data: the input data, which contains the schema header as well
        ├── mongodb: the output folder for gen.sh, also the input of load.sh
        ├── index.js: query script for mongo shell to create index, used by index.sh and load.sh
        ├── drop.js: query script for mongo shell to drop the database, used by drop.sh
        ├── customer.py: script to generate customer.csv to be inserted in cassandra from original tables
        ├── district.py: script to generate district.csv to be inserted in cassandra from original tables
        ├── item.py: script to generate item.csv to be inserted in cassandra from original tables
        ├── order2.py: script to generate order2.csv to be inserted in cassandra from original tables
        ├── warehouse.py: script to generate warehouse.csv to be inserted in cassandra from original tables
├── benchmark: the folder to do benchmarks
    ├── driver_lib: the dependent jar libs
    ├── driver.jar: the compiled java files
    ├── benchmark.py: script to do benchmark by inputing the database and number of clients, it will then generate the benchmark stats
    ├── D8|D40-xact: the transaction input files
├── benchmark_runner.sh: the single script to benchmark the for 6 cases (D8|D40 and 10|20|40 clients)
├── src: the java files
    ├── transactions: the file to do individule transactions
    ├── Driver.java: the entry main class to run the transaction by reading from stdin and output to stderr
├── pom.xml: the maven config file
```

## Configuration and run program
1. Compile the program:
    Open with Intellij or Eclipse, import as Maven projects with Java 8 as environment, make sure you can pull the maven dependencies from internet. If you don't need to change code, you can use the compiled code from benchmark folder. To regenerate, you can create a runnable jar from Intellij or Eclipse and get all the jar libs into a separate files. Put the driver.jar (compiled jar) and driver_lib/ (the lib jars) under benchmark/ folder

2. Generate the input files:
    Download the data files, put into corresponding load/load-D8/D8-data or load/load-D40/D40-data folder, input the schema header into the first line of each data file
    go into load/load-D8|D40 folder and run gen.sh to generate the table data file in cassandra folder. verify if files are generated

3. Organize the folders:
    upload the files and organized as following
    ```
    ├── load
        ├── load.sh
        ├── drop.sh
        ├── load-D8|D40
            ├── mongodb
                ├── the generated data files
            ├── drop.js
            ├── index.js
    ├── benchmark
        ├── driver_lib
            ├── all the jar libs
        ├── driver.jar
        ├── benchmark.py
    ├── benchmark_runner.sh
    ```
    Note: the mongodb bin is assumed to be at: /temp/mongodb-linux-x86_64-rhel70-3.2.9/bin under localhost

4. Start mongodb process with 1 replication. Make sure the mongodb is correctly started and ready to accept queries

5. Go to load folder and run load.sh to load the data into MongoDB. Make sure the data is correctly inserted.

6. Run the benchmark_runner
    run> nohup sh benchmark_runner.sh > benchmark.log 2>&1 &

7. Examing the result
    under the root folder of this project
    benchmark-{D8|D40}-{10|20|40}.log is the corresponding results for each benchmark

    under the benchmark folder, we have the {benchmark start timestamp} folder, which contains the benchmark outputs for each client running

8. Kill the mongodb process, start the mongodb process on three servers with replication. Make sure the mongodb is correctly started and ready to accept queries

9. Repeat 5 - 7 to get result of 3 replication benchmark