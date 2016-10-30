#!/usr/local/bin/python
import csv
import json

with open('mongodb/district.json', 'wb') as f:  # output csv file
    with open('D40-data/district.csv', 'r') as districtfile:  # input csv file
        districtdict = csv.DictReader(districtfile, delimiter=',')
        for row in districtdict:
            json.dump({'w_id':int(row["D_W_ID"]), 'd_id':int(row["D_ID"]), 'd_next_oid':int(row["D_NEXT_O_ID"]), 'd_ytd':float(row["D_YTD"])}, f)
            f.write('\n')
