# Three node MongoDB configurations

## Structure:
```
├── mongodb-linux-x86_64-rhel70-3.2.9: the mongodb configurations for 3 node sharding, this folder is assumed to be placed under /temp of xcnd9, xcnd10, xcnd11 servers
    ├── bin: the executables
    ├── db: the db folders
    ├── log: the log folders
    ├── *.config: the configurations for different kinds of mongonode
    ├── start.sh: script to start all the mongodb instances in three servers
    ├── start_*.sh: script to start the different mongodb instance in localhost
    ├── stop.sh: will stop the mongodb processes for all three servers
```