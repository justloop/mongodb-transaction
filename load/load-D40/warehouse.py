#!/usr/local/bin/python
import csv
import json

with open('mongodb/warehouse.json', 'wb') as f:  # output csv file
    with open('D40-data/warehouse.csv', 'r') as csvfile:  # input csv file
        reader = csv.DictReader(csvfile, delimiter=',')
        for row in reader:
            json.dump({'w_id': int(row["W_ID"]),'w_ytd': float(row["W_YTD"])},f)
            f.write('\n')
