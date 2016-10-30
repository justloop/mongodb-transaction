#!/usr/local/bin/python
import csv
import json

with open('mongodb/item.json', 'wb') as f:  # output csv file
    with open('D8-data/item.csv', 'r') as itemfile:  # input csv file
        with open('D8-data/stock.csv', 'r') as stockfile:  # input csv file
            itemdict = csv.DictReader(itemfile, delimiter=',')
            itemlist = list(itemdict)
            stockdict = csv.DictReader(stockfile, delimiter=',')

            for stock in stockdict:
                item = itemlist[int(stock['S_I_ID']) - 1]
                json.dump({'i_w_id': int(stock["S_W_ID"]), 'i_id': int(item["I_ID"]), 'i_name': item["I_NAME"], 'i_price': float(item["I_PRICE"]), 'i_im_id': int(item["I_IM_ID"]), 'i_data': item["I_DATA"], 's_quantity': int(stock["S_QUANTITY"]), 's_ytd': float(stock["S_YTD"]), 's_order_cnt': int(stock["S_ORDER_CNT"]), 's_remote_cnt': int(stock["S_REMOTE_CNT"]), 's_dist_01': stock["S_DIST_01"], 's_dist_02': stock["S_DIST_02"], 's_dist_03': stock["S_DIST_03"], 's_dist_04': stock["S_DIST_04"], 's_dist_05': stock["S_DIST_05"], 's_dist_06': stock["S_DIST_06"], 's_dist_07': stock["S_DIST_07"], 's_dist_08': stock["S_DIST_08"], 's_dist_09': stock["S_DIST_09"], 's_dist_10': stock["S_DIST_10"], 's_data': stock["S_DATA"]}, f)
                f.write('\n')
