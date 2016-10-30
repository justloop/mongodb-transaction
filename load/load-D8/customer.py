#!/usr/local/bin/python
import csv
import json
import hashlib

#customer to his last order
customerOrder = dict()
#warehouse id+district id to district
districtTable = dict()
with open('mongodb/customer.json', 'wb') as f:  # output csv file
    with open('D8-data/warehouse.csv', 'r') as warehousefile:  # input csv file
        with open('D8-data/district.csv', 'r') as districtfile:  # input csv file
            with open('D8-data/customer.csv', 'r') as customerfile:  # input csv file
                with open('D8-data/order.csv', 'r') as orderfile:  # input csv file
                    warehousedict = csv.DictReader(warehousefile, delimiter=',')
                    warehouselist = list(warehousedict)
                    districtdict = csv.DictReader(districtfile, delimiter=',')
                    districtlist = list(districtdict)
                    customerdict = csv.DictReader(customerfile, delimiter=',')
                    orderdict = csv.DictReader(orderfile, delimiter=',')

                    for order in orderdict:
                        customerOrder[order["O_W_ID"]+","+order["O_D_ID"]+","+order["O_C_ID"]] = order["O_ID"]

                    for district in districtlist:
                        districtTable[district["D_W_ID"]+","+district["D_ID"]] = district

                    for customer in customerdict:
                        district = districtTable[customer['C_W_ID']+","+customer['C_D_ID']]
                        warehouse = warehouselist[int(customer['C_W_ID']) - 1]
                        json.dump({'w_id': int(customer["C_W_ID"]), 'd_id': int(customer["C_D_ID"]), 'c_id': int(customer["C_ID"]), 'c_first': customer["C_FIRST"], 'c_middle': customer["C_MIDDLE"], 'c_last': customer["C_LAST"], 'c_street_1': customer["C_STREET_1"], 'c_street_2': customer["C_STREET_2"], 'c_city': customer["C_CITY"], 'c_state': customer["C_STATE"], 'c_zip': customer["C_ZIP"], 'c_phone': customer["C_PHONE"], 'c_since': customer["C_SINCE"], 'c_credit': customer["C_CREDIT"], 'c_credit_lim': float(customer["C_CREDIT_LIM"]), 'c_discount': float(customer["C_DISCOUNT"]), 'c_balance': float(customer["C_BALANCE"]), 'c_ytd_payment': float(customer["C_YTD_PAYMENT"]), 'c_payment_cnt': int(customer["C_PAYMENT_CNT"]), 'c_delivery_cnt': int(customer["C_DELIVERY_CNT"]), 'c_data': customer["C_DATA"], 'warehouse': {'w_name': warehouse["W_NAME"], 'w_street_1': warehouse["W_STREET_1"], 'w_street_2': warehouse["W_STREET_2"], 'w_city': warehouse["W_CITY"], 'w_state': warehouse["W_STATE"], 'w_zip': warehouse["W_ZIP"], 'w_tax': float(warehouse["W_TAX"])}, 'district': {'d_name': district["D_NAME"], 'd_street_1': district["D_STREET_1"], 'd_street_2': district["D_STREET_2"], 'd_city': district["D_CITY"], 'd_state': district["D_STATE"], 'd_zip': district["D_ZIP"], 'd_tax': float(district["D_TAX"])}, 'last_order_id': int(customerOrder[customer["C_W_ID"]+","+customer["C_D_ID"]+","+customer["C_ID"]])}, f)
                        f.write('\n')
