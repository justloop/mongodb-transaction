#!/usr/local/bin/python
import csv
import json

# store the result row
delivery = dict()
orderToline = dict()
with open('mongodb/order2.json', 'wb') as f:  # output csv file
    with open('D8-data/order-line.csv', 'r') as orderlinefile:  # input csv file
        with open('D8-data/order.csv', 'r') as orderfile:  # input csv file
            with open('D8-data/customer.csv', 'r') as customerfile:  # input csv file
                with open('D8-data/item.csv', 'r') as itemfile:  # input csv file
                    orderlinedict = csv.DictReader(orderlinefile, delimiter=',')
                    orderdict = csv.DictReader(orderfile, delimiter=',')
                    itemdict = csv.DictReader(itemfile, delimiter=',')
                    itemlist = list(itemdict)
                    customerdict = csv.DictReader(customerfile, delimiter=',')
                    customerlist = list(customerdict)
                    
                    for orderline in orderlinedict:
                        item = itemlist[int(orderline['OL_I_ID'])-1]
                        orderlineobj = {'ol_i_id': int(orderline['OL_I_ID']), 'ol_i_name': item['I_NAME'], 'ol_amount': float(orderline['OL_AMOUNT']), 'ol_supply_w_id': int(orderline['OL_SUPPLY_W_ID']), 'ol_quantity': int(orderline['OL_QUANTITY']), 'ol_dist_info': orderline['OL_DIST_INFO']}
                        if (orderline['OL_W_ID']+","+orderline['OL_D_ID']+","+orderline['OL_O_ID']) in orderToline:
                            orderToline[(orderline['OL_W_ID']+","+orderline['OL_D_ID']+","+orderline['OL_O_ID'])].append(orderlineobj)
                        else:
                            orderToline[(orderline['OL_W_ID']+","+orderline['OL_D_ID']+","+orderline['OL_O_ID'])] = []
                            orderToline[(orderline['OL_W_ID']+","+orderline['OL_D_ID']+","+orderline['OL_O_ID'])].append(orderlineobj)
                            delivery[(orderline['OL_W_ID']+","+orderline['OL_D_ID']+","+orderline['OL_O_ID'])] = orderline['OL_DELIVERY_D']

                    print "customer rows =\n", len(customerlist)
                    for order in orderdict:
                        #print "order =\n", order
                        customer = customerlist[int(order['O_C_ID']) - 1]
                        orderlineArr = orderToline[order['O_W_ID']+","+order['O_D_ID']+","+order['O_ID']]
			carrier_id = order['O_CARRIER_ID']
			if carrier_id == 'null':
                            carrier_id = '-1'
                        json.dump({'o_w_id': int(order['O_W_ID']), 'o_d_id': int(order['O_D_ID']), 'o_id': int(order['O_ID']), 'o_c_id': int(order['O_C_ID']), 'customer': {'c_first': customer['C_FIRST'], 'c_middle': customer['C_MIDDLE'], 'c_last': customer['C_LAST']}, 'o_carrier_id': int(carrier_id), 'o_ol_cnt': int(order['O_OL_CNT']), 'o_all_local': int(order['O_ALL_LOCAL']), 'o_entry_d': order['O_ENTRY_D'], 'ol_delivery_d': delivery[order['O_W_ID']+","+order['O_D_ID']+","+order['O_ID']],'orderlines':orderlineArr}, f)
                        f.write('\n')
